def notifySlack(String buildStatus = 'STARTED') {
    // Build status of null means success.
    buildStatus = buildStatus ?: 'SUCCESS'

    def color

    if (buildStatus == 'STARTED') {
        color = '#D4DADF'
    } else if (buildStatus == 'SUCCESS') {
        color = '#BDFFC3'
    } else if (buildStatus == 'UNSTABLE') {
        color = '#FFFE89'
    } else {
        color = '#FF9FA1'
    }

    def msg = "${buildStatus}: `${env.JOB_NAME}` #${env.BUILD_NUMBER}:\n${env.BUILD_URL}"

    slackSend(color: color, message: msg)
}
pipeline{
    agent any
    tools{
        maven "maven3.8.4"
    }

 stages{
        stage('checkout code'){
        steps{
            notifySlack('STARTED')
            git credentialsId: 'b0093679-6fc3-4399-87a0-121d50e0a9fc', url: 'https://github.com/harishvarma1122/maven-web-application.git'
        }
        }
 }
 post {
  success {
      notifySlack(currentBuild.result)
    // One or more steps need to be included within each condition's block.
  }
  failure {
      notifySlack(currentBuild.result)
    // One or more steps need to be included within each condition's block.
  }
 
}
}
