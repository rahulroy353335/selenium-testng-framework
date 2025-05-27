pipeline {
    agent any
    tools {
        maven 'Maven 3.8.8' // Match your Jenkins Maven setup
        jdk 'jdk21' // Match your Jenkins JDK setup
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
            // Archive test logs (optional)
            archiveArtifacts artifacts: 'target\\surefire-reports\\*.xml, target\\*.jar', allowEmptyArchive: true
        }
        failure {
            mail to: 'rajaroy353335@gmail.com',
             subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: """
                Build failed. Check details at: ${env.BUILD_URL}
                 
                 Last 50 lines of logs:
                  ${bat(script: 'powershell -command "Get-Content target\\surefire-reports\\*.txt -Tail 50 2>$null || echo \'No logs found\'"', returnStdout: true)}
                 """
        }
        success {
            mail to: 'rajaroy353335@gmail.com',
             subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
             body: "See build: ${env.BUILD_URL}"
        }
    }
}