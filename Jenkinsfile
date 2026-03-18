pipeline {
    agent any

    tools {
        maven 'Maven_3'
    }

    parameters {
        booleanParam(
        name: 'RUN_UI_TESTS',
        defaultValue: false,
        description: 'Run Selenium UI tests')
        booleanParam(
        name: 'RUN_INTEGRATION_TESTS',
        defaultValue: true,
        description: 'Run API/Integration tests (Karate)')
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
                    bat '''
                    mvn sonar:sonar ^
                    -Dsonar.projectKey=library-management-system ^
                    -Dsonar.projectName="Library Management System" ^
                    -Dsonar.host.url=%SONAR_HOST_URL% ^
                    -Dsonar.login=%SONAR_AUTH_TOKEN% ^
                    -Dsonar.coverage.jacoco.xmlReportPaths=target\\site\\jacoco\\jacoco.xml
                    '''
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
                bat 'mvn verify'
            }
        }

    }

    post {
        always {
            echo 'Publishing reports...'

            //Unit test reports
            junit '**/target/surefire-reports/*.xml'

            //Integration test reports
            junit '**/target/failsafe-reports/*.xml'

            // Archive JAR
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true

            // 1. JACOCO COVERAGE TREND
            recordCoverage(
                tools: [[parser: 'JACOCO', pattern: '**/jacoco.xml']]
            )

            // 2. HTML COVERAGE REPORT
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/site/jacoco',
                reportFiles: 'index.html',
                reportName: 'JaCoCo Coverage Report'
            ])

            // 3. KARATE REPORT
            publishHTML(target: [
                allowMissing: true,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'target/karate-reports',
                reportFiles: 'karate-summary.html',
                reportName: 'Karate API Test Report'
            ])
        }
    }
}