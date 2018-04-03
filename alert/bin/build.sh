#!/bin/bash
#Constants
MODULE_NAME="alert"
cd ${MODULE_NAME}
SERVICE_NAME="monitor-alert"
REPOSITORY_NAME="/elk/monitor-alert"
REPOSITORY_HOST=34.213.137.99:8888
REPOSITORY_URI=$REPOSITORY_HOST$REPOSITORY_NAME

docker build -t $SERVICE_NAME .
docker tag $SERVICE_NAME $REPOSITORY_URI:v_$BUILD_NUMBER
docker push $REPOSITORY_URI:v_$BUILD_NUMBER

#Replace the build number and respository URI placeholders with the constants above
sed -e "s;%BUILD_NUMBER%;${BUILD_NUMBER};g" -e "s;%REPOSITORY_URI%;${REPOSITORY_URI};g" monitor-deploy.yaml > ${SERVICE_NAME}-v_${BUILD_NUMBER}.yaml
#Register the task definition in the repository

ssh k8s-master "mkdir -p /opt/k8s/monitor/delpoy/"
scp ${SERVICE_NAME}-v_${BUILD_NUMBER}.yaml k8s-master:/opt/k8s/monitor/delpoy/

ssh k8s-master "kubectl create -f /opt/k8s/monitor/delpoy/${SERVICE_NAME}-v_${BUILD_NUMBER}"

echo "Testing"
