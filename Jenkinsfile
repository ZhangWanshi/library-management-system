pipeline {
    agent any

    tools {
        maven 'Maven_3'
    }

    parameters {
        booleanParam(name: 'RUN_UI_TESTS', defaultValue: false, description: 'Run Selenium UI tests')
        booleanParam(name: 'RUN_INTEGRATION_TESTS', defaultValue: true, description: 'Run API/Integration tests (Karate)')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Unit Tests') {
            steps {
                echo 'Running Unit Tests (JUnit + Mockito)'
                bat 'mvn clean test'
            }
        }

        stage('Integration/API Tests (Karate)') {
            when {
                expression { params.RUN_INTEGRATION_TESTS }
            }
            steps {
                echo 'Running Integration/API Tests (Karate)'
                bat 'mvn test -Dtest=KarateRunnerIT'
            }
        }

        stage('Build & Coverage') {
            steps {
                echo 'Building project and generating JaCoCo coverage'
                // package + JaCoCo report
                bat 'mvn package jacoco:report'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube Analysis'
                withSonarQubeEnv('SonarServer') {
                    // Windows bat + JaCoCo XML path, Quality Gate will read this
                    bat 'mvn sonar:sonar -Dsonar.coverage.jacoco.xmlReportPaths=target\\site\\jacoco\\jacoco.xml ' +
                        '-Dsonar.projectKey=library-management-system ' +
                        '-Dsonar.projectName="Library Management System" ' +
                        '-Dsonar.host.url=%SONAR_HOST_URL% ' +
                        '-Dsonar.login=%SONAR_AUTH_TOKEN%'
                }
            }
        }

        stage('Wait Before Quality Gate') {
            steps {
                echo 'Waiting for SonarQube to process analysis...'
                sleep(time: 20, unit: 'SECONDS')
            }
        }

        stage('Quality Gate') {
            steps {
                echo 'Waiting for SonarQube Quality Gate (Coverage >= 80%)'
                timeout(time: 15, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('UI Tests (Selenium)') {
            when {
                expression { params.RUN_UI_TESTS }
            }
            steps {
                echo 'Running UI Tests (Selenium)'
                bat 'mvn verify -DskipUnitTests=true'
            }
        }

    }

    post {
        always {
            echo 'Archiving test reports and artifacts'
            // JUnit unit tests
            junit '**/target/surefire-reports/*.xml'
            // Failsafe integration tests
            junit '**/target/failsafe-reports/*.xml'
            // Archive built JARs
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            // Archive coverage reports
            archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
            //HTML Coverage Report
        }
    }
}