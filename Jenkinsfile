pipeline{

agent any

stages{

stage('Checkout'){

steps{

git branch: "main", url: 'https://github.com/mdtauqeeralam21/appointment-service.git'

}

}

stage('Build'){

steps{

sh 'mvn clean package -DskipTests=true'

}

post{

always{

archiveArtifacts 'target/*.jar'

}

}

}

stage('DockerBuild') {

steps {

sh 'docker build -t tauqeeralam21/appointment-service:latest .'

}

}

stage('Login') {

steps {

sh 'echo Tauqeer@786 | docker login -u tauqeeralam21 --password-stdin'

}

}

stage('Push') {

steps {

sh 'docker push tauqeeralam21/appointment-service'

}

}

}

post {

always {

sh 'docker logout'

}

}

}

