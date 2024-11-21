pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the Git repository
                git 'https://github.com/zerorequiem20/DataStripper.git'
            }
        }

        stage('Build and Test') {
            steps {
                script {
                    // Run Maven build (this will compile and run tests)
                    sh 'mvn clean install'
                }
            }
        }

        stage('Deploy') {
            steps {
                // Add steps for deploying the app if needed
            }
        }
    }
}
