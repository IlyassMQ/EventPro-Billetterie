
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build, Test and Coverage') {
            steps {
                sh 'chmod +x mvnw && ./mvnw clean verify'
            }
        }
    }


post {
    always {
        junit testResults: 'target/surefire-reports/*.xml',
              allowEmptyResults: true

        archiveArtifacts artifacts: 'target/site/jacoco/**',
                         allowEmptyArchive: true

        publishHTML(target: [
            reportDir: 'target/site/jacoco',
            reportFiles: 'index.html',
            reportName: 'JaCoCo Coverage Report',
            keepAll: true,
            alwaysLinkToLastBuild: true,
            allowMissing: false
        ])
    }
}

}
