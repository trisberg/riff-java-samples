# wordcount

riff wordcount function

## Build

```
riff function create wordcount-boot --handler wordcount \
  --git-repo https://github.com/trisberg/riff-java-samples.git \
  --sub-path hwordcount-boot \
  --tail
```

## Deploy

```
riff knative deployer create wordcount --function-ref wordcount-boot \
  --ingress-policy External \
  --env spring_main_web_application_type=NONE \
  --tail
```

## Invoke

```
ingress=$(kubectl get svc envoy-external --namespace projectcontour --output 'jsonpath={.status.loadBalancer.ingress[0].ip}')
curl ${ingress} -H 'Host: wordcount.default.example.com' -H 'Content-Type: application/json' -H 'Accept: text/plain' -d '"riff"' && echo
```

## Teardown

```
riff knative deployer delete wordcount
riff function delete wordcount-boot
```

## Local test

```
./mvnw spring-boot:run
curl localhost:8080 -H 'Content-Type: application/json' -H 'Accept: text/plain' -d '"riff"' && echo
```

## starting from scratch

To initialize the function source:

```sh
curl https://start.spring.io/starter.zip?type=maven-project \
  -d language=java \
  -d platformVersion=2.3.0.M2 \
  -d packaging=jar \
  -d jvmVersion=11 \
  -d groupId=com.example \
  -d artifactId=wordcount \
  -d name=wordcount \
  -d description='Wordcount function for riff' \
  -d packageName=functions \
  -d dependencies=webflux,cloud-function,actuator | tar -xzvf -
```
