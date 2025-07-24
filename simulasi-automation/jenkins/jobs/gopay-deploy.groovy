pipeline {
  agent any

  stages {
    stage('Build & Push GoPay') {
      steps {
        dir('gopay-service') {
          sh 'docker build -t kautsarakasyah/gopay-service:latest .'
          sh 'docker push kautsarakasyah/gopay-service:latest'
        }
      }
    }

    stage('Deploy to OpenShift') {
      steps {
        sh 'oc rollout restart deployment gopay -n final-app-deployment'
      }
    }
  }
}
