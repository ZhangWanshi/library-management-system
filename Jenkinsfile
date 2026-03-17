pipeline {
  agent any

  tools {
    maven 'Maven_3'
  }

  parameters {
    booleanParam(name: 'RUN_UI_TESTS', defaultValue: false)
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Unit Tests') {
      steps {
        bat 'mvn clean test'
      }
    }

    stage('API Tests (Karate)') {
      steps {
        bat 'mvn test -Dtest=KarateRunnerIT'
      }
    }

    stage('Build & Coverage') {
      steps {
        bat 'mvn package jacoco:report'
      }
    }

    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('SonarServer') {
          bat 'mvn sonar:sonar'
        }
      }
    }

    stage('Quality Gate') {
      steps {
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    stage('UI Tests (Selenium)') {
      when {
        expression { params.RUN_UI_TESTS }
      }
      steps {
        bat 'mvn verify -DskipUnitTests=true'
      }
    }
  }

  post {
    always {
       // Unit tests
       junit '**/target/surefire-reports/*.xml'
       // Integration tests (Failsafe + Karate + Selenium)
       junit '**/target/failsafe-reports/*.xml'
       junit '**/target/karate-reports/*.xml'
       archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
    }
  }
}