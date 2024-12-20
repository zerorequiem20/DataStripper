pipeline {
    agent any

    stages {
//         stage('Build') {
//             steps {
//                 echo 'Building the project...'
//                 sh 'mvn clean install'
//             }
//         }
//
        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying the application...'
                // Add your deploy steps here
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished!'
        }
    }
}