pipeline {
    agent any

    environment {
        APP_JAR = 'adm-data-stripper-1.0-SNAPSHOT.jar'   // Built JAR name
        DOCKER_HOST = '13.48.6.128'                      // Docker EC2 public IP
        DOCKER_USER = 'ubuntu'                           // SSH username
        DOCKER_DIR = '~/docker-app'                      // Directory on Docker EC2
        DOCKER_IMAGE = 'myapp:latest'                    // Docker image name
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/zerorequiem20/DataStripper.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: "target/${APP_JAR}", fingerprint: true
            }
        }

        stage('Deploy to Docker Host') {
            steps {
                // Use SSH private key credential
                withCredentials([sshUserPrivateKey(credentialsId: 'docker-ssh-key', keyFileVariable: 'SSH_KEY')]) {
                    // Ensure Docker directory exists
                    sh "ssh -i $SSH_KEY ${DOCKER_USER}@${DOCKER_HOST} 'mkdir -p ${DOCKER_DIR}'"

                    // Copy JAR to Docker host
                    sh "scp -i $SSH_KEY target/${APP_JAR} ${DOCKER_USER}@${DOCKER_HOST}:${DOCKER_DIR}/"

                    // Build Docker image, stop & remove old container, then run new one
                    sh """
                        ssh -i $SSH_KEY ${DOCKER_USER}@${DOCKER_HOST} '
                            docker build -t ${DOCKER_IMAGE} ${DOCKER_DIR} &&
                            docker stop myapp || true &&
                            docker rm myapp || true &&
                            docker run -d --name myapp -p 8080:8080 ${DOCKER_IMAGE}
                        '
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Build, tests, and deployment successful!'
        }
        failure {
            echo 'Pipeline failed at some stage.'
        }
        always {
            echo 'Pipeline finished.'
        }
    }
}