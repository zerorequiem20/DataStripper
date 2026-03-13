pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/zerorequiem20/DataStripper.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

    }

    post {
        success {
            echo 'Build and tests successful!'
        }

        failure {
            echo 'Build or tests failed.'
        }

        always {
            echo 'Pipeline finished.'
        }
    }
}