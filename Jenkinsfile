pipeline {
    agent {
        docker {
            image 'maven:3.8.8-eclipse-temurin-21'
            args '-v /tmp/.X11-unix:/tmp/.X11-unix -e DISPLAY=:99'
        }
    }
    tools {
        maven 'Maven 3.8.8' // Match your Jenkins Maven setup
        jdk 'jdk21' // Match your Jenkins JDK setup
    }
    environment {
        // SMTP Configuration (Gmail)
        MAIL_SMTP_HOST = 'smtp.gmail.com'
        MAIL_SMTP_PORT = '587'
        MAIL_SMTP_USER = 'rahulroy531990@gmail.com'
        MAIL_SMTP_AUTH = 'true'
        MAIL_SMTP_STARTTLS_ENABLE = 'true'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'ciintegration', url: 'https://github.com/rahulroy353335/selenium-testng-framework.git'
            }
        }

        stage('Build & Test') {
            steps {
                // Run tests in headless mode (Linux example)
                bat 'mvn clean test -Dtest=FirefoxTest'  // Use 'bat' instead of 'sh'
                bat 'dir /s target\\'  // Windows directory listing
            }
            
        }
    }
    post {
      always {
        archiveArtifacts artifacts: 'target\\surefire-reports\\*.xml', allowEmptyArchive: true
    }
    failure {
      script {   
        withCredentials([string(credentialsId: 'gmail-smtp-password', variable: 'SMTP_SECRET')]) {
                env.MAIL_SMTP_PASSWORD = SMTP_SECRET
        emailext (
            subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
            Check build: ${env.BUILD_URL}
            Last 50 lines of logs:
            ${bat(script: 'powershell -command "Get-Content target\\surefire-reports\\*.txt -Tail 50 2>$null || echo \'No logs found\'"', returnStdout: true)}
            """,
            to: 'rajaroy353335@gmail.com',
            attachLog: true
        )
            }
      }
    }
    success {
      script {  
        withCredentials([string(credentialsId: 'gmail-smtp-password', variable: 'SMTP_SECRET')]) {
                env.MAIL_SMTP_PASSWORD = SMTP_SECRET
        emailext (
            subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: "Build succeeded: ${env.BUILD_URL}",
            to: 'rajaroy353335@gmail.com'
        )
        }
    }
     }
}
}