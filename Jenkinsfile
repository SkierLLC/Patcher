pipeline {
  agent any
   environment {
          DEPLOY_TOKEN     = credentials('mod_deploy_token')
      }
  stages {
    stage('Initialize') {
      steps {
        sh 'chmod +x ./gradlew'
//         sh "./gradlew preprocessResources"
      }
    }
    stage('Build') {
      steps {
        sh "./gradlew publish -PBUILD_ID=${env.BUILD_ID} -Pbranch=${env.BRANCH_NAME} -PIS_CI=true --no-daemon"
        sh "./gradlew build -PBUILD_ID=${env.BUILD_ID} -Pbranch=${env.BRANCH_NAME} -PIS_CI=true --no-daemon"
      }
    }

    stage('Report') {
      steps {
        archiveArtifacts 'versions/1.8.9/build/libs/*.jar'
        archiveArtifacts 'versions/1.12.2/build/libs/*.jar'
      }
    }
    stage('Notify') {
        steps {
            sh "java -jar deploy.jar ${JENKINS_HOME} ${env.JOB_NAME} ${env.BUILD_ID} ${DEPLOY_TOKEN}"
        }
    }
  }
}
