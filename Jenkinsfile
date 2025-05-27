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
                sh 'ls -la target/'
            }
            
        }
    }
    post {

        always {
            // Archive test logs (optional)
            archiveArtifacts artifacts: 'target/surefire-reports/*.xml, target/*.jar', allowEmptyArchive: true
        }
        failure {
            mail to: 'rajaroy353335@gmail.com',
             subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: """
                 Build failed. Check details at: ${env.BUILD_URL}
                 
                 Last 50 lines of logs:
                 ${sh(script: 'tail -50 target/surefire-reports/*.txt || echo "No logs found"', returnStdout: true)}
                 """
        }
        success {
            mail to: 'rajaroy353335@gmail.com',
             subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
             body: "See build: ${env.BUILD_URL}"
        }
    }
}