
pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'eventpro-postgres',
                    usernameVariable: 'DB_USER',
                    passwordVariable: 'DB_PASSWORD'
                )]) {

                    withEnv([
                        'SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/eventpro_db',
                        "SPRING_DATASOURCE_USERNAME=${DB_USER}",
                        "SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}"
                    ]) {

                        sh 'chmod +x mvnw && ./mvnw clean test'
                    }
                }
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml',
                  allowEmptyResults: true
        }
    }
}
