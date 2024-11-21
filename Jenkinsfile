pipeline {
    agent any

    environment {
        // Define environment variables if necessary
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Add the build commands here
                    echo 'Building the project...'
                    sh 'mvn clean install'  // Example for Maven
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    // Add test commands here
                    echo 'Running tests...'
                    sh 'mvn test'  // Example for running unit tests with Maven
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Add deploy commands here
                    echo 'Deploying the application...'
                    // Example deployment command (e.g., to an EC2 instance)
                }
            }
        }
    }

    post {
        always {
            // Post-build actions such as cleanup
            echo 'This will always run'
        }
    }
}
