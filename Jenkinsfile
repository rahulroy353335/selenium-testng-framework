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
                sh '''
                  export DISPLAY=:99
                  Xvfb :99 -screen 0 1024x768x24 &
                  mvn clean test -Dtest=FirefoxTest
                '''
            }
            post {
                always {
                    publishHTML(
                        target: [
                            allowMissing: false,
                            alwaysLinkToLastBuild: false,
                            keepAll: true,
                            reportDir: 'target/surefire-reports', // Path to your HTML reports
                            reportFiles: 'index.html',            // Main report file (e.g., TestNG, JUnit)
                            reportName: 'Test Results'
                                ]
                                )
                        }
                    }
        }
    }
    post {
        failure {
            slackSend channel: '#alerts', message: "❌ Build Failed: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        }
        success {
            slackSend channel: '#alerts', message: "✅ Build Succeeded: ${env.JOB_NAME} ${env.BUILD_NUMBER}"
        }
    }
}