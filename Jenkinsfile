pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'datastripper'  // Your Docker image name
        DOCKER_TAG = 'v1.0'    // You can replace 'latest' with any version tag
        GIT_REPO = 'https://github.com/zerorequiem20/DataStripper.git' // GitHub Repo URL
    }

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    // Clone the repository from GitHub, explicitly specifying the branch
                    checkout([$class: 'GitSCM',
                              branches: [[name: 'refs/heads/main']],
                              userRemoteConfigs: [[url: "${GIT_REPO}"]]
                    ])
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image from the Dockerfile in the repo
                    def app = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }

        stage('Test Docker Image') {
            steps {
                script {
                    // Test the Docker image by running it and ensuring it works
                    docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").inside {
                        sh 'mvn test'  // Run tests inside the container
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Stop and remove existing containers with the same name
                    sh 'docker ps -q -f name=datastripper || true | xargs docker stop | xargs docker rm || true'

                    // Deploy Docker image
                    docker.run("${DOCKER_IMAGE}:${DOCKER_TAG}", "-d -p 8080:8080")
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished!'
        }
    }
}
