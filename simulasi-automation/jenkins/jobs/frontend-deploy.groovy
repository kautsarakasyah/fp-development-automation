pipeline {
  agent any

  environment {
    IMAGE_NAME = 'kautsarakasyah/fp-frontend:latest'
    DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
    TELEGRAM_BOT_TOKEN = '7964045222:AAElE5m35X1rUfU-2lO0ZpLzwuy_esmMsvY'
    TELEGRAM_CHAT_ID = '1868802578'
    OPENSHIFT_DEPLOYMENT_NAME = 'fp-frontend'
    OPENSHIFT_NAMESPACE = 'final-app-deployment'
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: 'main', url: 'https://github.com/kautsarakasyah/fp-development-automation.git'
      }
    }

    stage('Build Docker Image') {
      steps {
        dir('fp-frontend-fix') {
          echo "📦 Building Docker image: ${env.IMAGE_NAME}"
          sh "docker build -t ${env.IMAGE_NAME} ."
        }
      }
    }

    stage('Push to DockerHub') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
          sh """
            echo $PASS | docker login -u $USER --password-stdin
            docker push ${env.IMAGE_NAME}
          """
        }
      }
    }

    stage('Deploy to OpenShift') {
      steps {
        sh "oc rollout restart deployment ${env.OPENSHIFT_DEPLOYMENT_NAME} -n ${env.OPENSHIFT_NAMESPACE}"
      }
    }
  }

  post {
    success {
      echo "✅ Deployment Success"
      sh """
        curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage \
        -d chat_id=${env.TELEGRAM_CHAT_ID} \
        -d text="✅ [Jenkins] Deploy *fp-frontend* berhasil!" \
        -d parse_mode=Markdown
      """
    }

    failure {
      echo "❌ Deployment Failed"
      sh """
        curl -s -X POST https://api.telegram.org/bot${env.TELEGRAM_BOT_TOKEN}/sendMessage \
        -d chat_id=${env.TELEGRAM_CHAT_ID} \
        -d text="❌ [Jenkins] Deploy *fp-frontend* gagal!" \
        -d parse_mode=Markdown
      """
    }
  }
}
