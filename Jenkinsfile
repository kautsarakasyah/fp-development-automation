// Jenkinsfile (Declarative Pipeline)
// Dirancang untuk Monorepo: BNI App (Next.js), GoPay Service (Java), ShopeePay Service (Go)

pipeline {
    // Menentukan di mana pipeline akan dieksekusi. 'any' berarti Jenkins bisa menggunakan agent mana saja.
    agent any

    // Mendefinisikan tahapan-tahapan dalam pipeline CI/CD
    stages {
        // Stage utama untuk membangun dan menguji semua layanan secara bersamaan (paralel)
        stage('Build & Test All Services') {
            parallel {
                // Stage untuk layanan GoPay (Spring Boot)
                stage('Build & Test GoPay Service') {
                    steps {
                        script {
                            echo "========================================"
                            echo "Building and Testing GoPay Service (Java)"
                            echo "========================================"
                            // Pindah ke direktori gopay-service
                            dir('gopay-service') {
                                // Menjalankan perintah Maven untuk membersihkan, menginstal dependensi, dan menjalankan test
                                // Menggunakan bat untuk Windows, sh untuk Linux/macOS
                                if (isUnix()) {
                                    sh './mvnw clean install'
                                } else {
                                    bat '.\\mvnw.cmd clean install'
                                }
                            }
                        }
                    }
                }

                // Stage untuk layanan ShopeePay (GoLang)
                stage('Build & Test ShopeePay Service') {
                    steps {
                        script {
                            echo "=========================================="
                            echo "Building and Testing ShopeePay Service (Go)"
                            echo "=========================================="
                            // Pindah ke direktori shopeepay-service
                            dir('app/microservices/shopeepay-golang') {
                                // Menyiapkan environment Go
                                // Anda mungkin perlu mengkonfigurasi Go Tool di Jenkins Global Tool Configuration
                                // tool name: 'go1.21' // Contoh
                                
                                echo "--- Running Go Mod Tidy ---"
                                sh 'go mod tidy'
                                
                                echo "--- Running Go Tests ---"
                                // Menjalankan unit test dengan output verbose dan coverage
                                sh 'go test -v ./...'
                                
                                echo "--- Building Go App ---"
                                // Membangun aplikasi Go
                                sh 'go build -o shopeepay-service-prod .'
                            }
                        }
                    }
                }

                // Stage untuk aplikasi BNI (Next.js)
                stage('Build BNI App') {
                    steps {
                        script {
                            echo "========================================"
                            echo "Building BNI App (Next.js)"
                            echo "========================================"
                            // Pindah ke direktori bni-app
                            dir('bni-app') {
                                // Menginstal dependensi npm
                                sh 'npm install'
                                // Membangun aplikasi Next.js untuk produksi
                                sh 'npm run build'
                            }
                        }
                    }
                }
            }
        }

        // Anda bisa menambahkan stage lain di sini, misalnya 'Deploy to Staging' atau 'Deploy to Production'
        // Contoh:
        // stage('Deploy to Staging') {
        //     steps {
        //         echo "Deploying all services to staging environment..."
        //         // Tambahkan skrip deployment Anda di sini
        //     }
        // }
    }

    // Blok post-build untuk tindakan yang dijalankan setelah pipeline selesai
    post {
        always {
            echo "Pipeline finished."
            // Membersihkan workspace untuk menghemat ruang disk
            cleanWs()
        }
        success {
            echo "Pipeline executed successfully!"
            // Kirim notifikasi sukses (misal: ke Slack atau email)
        }
        failure {
            echo "Pipeline failed!"
            // Kirim notifikasi kegagalan
        }
    }
}
