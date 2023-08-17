pipeline {
    agent any

    tools {
        maven "maven3.9.4"
    }

    stages {
        stage('Git pull') {
            steps {
                git branch: 'main', url: 'https://github.com/diac/ydeas'
            }
        }

        stage('Validate') {
            steps {
                sh "mvn validate"
            }
        }

        stage('Test') {
            steps {
                sh "mvn clean test -Ptest"
            }
        }

        stage('Build') {
            steps {
                sh "docker compose build"
            }
        }

        stage('Publish') {
            steps {
                sh "docker compose push"
            }
        }
    }
}