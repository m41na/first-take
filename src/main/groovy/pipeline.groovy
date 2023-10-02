import static works.hop.first.Dsl.pipeline

pipeline {

    agent any

    environment {
        SOME_NUMBER = 123
        SOME_STRING = "foobar"
    }

    stages {
        stage("Build") {
            steps { env ->
                sh "ls -al"
                sh(script: 'date +%Y-%m-%d', returnStdout: false)
                echo "Groovy rocks!"
                echo "env.SOME_STRING = ${env.SOME_STRING}"
            }
        }
        stage("Test") {
            steps {
                sh "java --version"
                sh "v -version"
            }
        }
    }
}