// vim: et sts=-1 sw=4
pipeline {
    agent any

    tools {
        maven "mvn3.6.3"
    }

    stages {
        stage('Build') {
            steps {
                dir('fastdfs-client-java') {
                    checkout([$class: 'GitSCM', branches: [[name: 'refs/tags/V1.27']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/happyfish100/fastdfs-client-java.git']]])
                    sh "mvn -Dmaven.test.failure.ignore=true clean install"
                }
                dir('fastdfs-restful') {
                    git 'https://github.com/blueocci/fastdfs-restful'
                    sh "mvn -Dmaven.test.failure.ignore=true clean package"
                }
            }

            post {
                success {
                    dir('fastdfs-restful') {
                        junit '**/target/surefire-reports/TEST-*.xml'
                        archiveArtifacts 'target/*.jar'
                    }
                }
            }
        }
    }
}
