pipeline {
    agent any

    environment {
        APP_JAR = 'adm-data-stripper-1.0-SNAPSHOT.jar'  // Name of the built JAR
        DOCKER_USER = 'ubuntu'                           // Docker EC2 username
        DOCKER_HOST = '13.49.89.11'                     // Docker EC2 public IP
        DOCKER_DIR = '~/docker-app'                      // Directory on Docker EC2
        DOCKER_IMAGE = 'myapp:latest'                   // Docker image name
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
                sshagent(credentials: ['docker-ssh-key']) {
                    // ensure Docker directory exists on the remote
                    sh "ssh -o StrictHostKeyChecking=no ${DOCKER_USER}@${DOCKER_HOST} 'mkdir -p ${DOCKER_DIR}'"

                    // Copy the JAR and Dockerfile to remote Docker host
                    sh "scp -o StrictHostKeyChecking=no target/${APP_JAR} Dockerfile ${DOCKER_USER}@${DOCKER_HOST}:${DOCKER_DIR}/"

                    // Build Docker image, stop & remove old container, run new container
                    sh """
                    ssh -o StrictHostKeyChecking=no ${DOCKER_USER}@${DOCKER_HOST} '
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