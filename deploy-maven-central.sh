#!/bin/bash

SECRETS=$(curl -sS -H "X-Vault-Token: $VAULT_TOKEN" -X GET https://vault.yildiz-games.be/v1/kv/yildiz-engine)

#Some variables need to be exported as env variable to be used by external processes.

export GH_TOKEN=$(echo $SECRETS | jq -r '.data.GH_TOKEN')
OPENSSL_PWD=$(echo $SECRETS | jq -r '.data.OPENSSL_PWD')
export GPG_KEY=$(echo $SECRETS | jq -r '.data.GPG_KEY')
export GPG_PWD=$(echo $SECRETS | jq -r '.data.GPG_PWD')
export OSSRH_USER_TOKEN=$(echo $SECRETS | jq -r '.data.OSSRH_USER_TOKEN')
export OSSRH_PWD_TOKEN=$(echo $SECRETS | jq -r '.data.OSSRH_PWD_TOKEN')
export REPO_USER=$(echo $SECRETS | jq -r '.data.REPO_USER')
export REPO_PASSWORD=$(echo $SECRETS | jq -r '.data.REPO_PASSWORD')
SONAR=$(echo $SECRETS | jq -r '.data.SONAR')
SONAR_ORGANIZATION=$(echo $SECRETS | jq -r '.data.SONAR_ORGANIZATION')

echo "Building $BRANCH branch"

if [ "$BRANCH" = "develop" ]; then
  openssl version -a
  openssl aes-256-cbc -pass pass:$OPENSSL_PWD -in ../build-resources/private-key.gpg.enc -out private-key.gpg -d && gpg --import --batch private-key.gpg && mvn -V -s ../build-resources/settings.xml org.jacoco:jacoco-maven-plugin:prepare-agent clean deploy sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.organization=$SONAR_ORGANIZATION -Dsonar.login=$SONAR
elif [ "$BRANCH" = "master" ]; then
  openssl version -a
  openssl aes-256-cbc -pass pass:$OPENSSL_PWD -in ../build-resources/private-key.gpg.enc -out private-key.gpg -d && gpg --import --batch private-key.gpg && mvn -V -s ../build-resources/settings.xml clean deploy
  mvn -V -s ../build-resources/settings.xml deploy -Dmaven.plugin.nexus.skip
else
  mvn -V -s ../build-resources/settings.xml clean package
fi
