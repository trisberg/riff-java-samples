# hello-message

Message sample function for riff

## build

```sh
riff function create hello-message \
   --handler hello \
  --git-repo https://github.com/trisberg/riff-java-samples.git \
  --sub-path hello-message \
  --tail
```

## deploy

```sh
riff knative deployer create hello-message --function-ref hello-message \
  --ingress-policy External \
  --env spring_main_web_application_type=NONE \
  --tail
```

## invoke

```sh
ingress=$(kubectl get svc envoy-external --namespace projectcontour --output 'jsonpath={.status.loadBalancer.ingress[0].ip}')
curl -X POST \
  -H 'Host: hello-message.default.example.com' \
  -H 'Content-Type: application/json' \
  -H 'Accept: text/plain' \
  -H "ce-specversion: 1.0" \
  -H "ce-source: curl-command" \
  -H "ce-type: curl.demo" \
  -H "ce-id: 123-abc" \
  -d '"test"' \
  ${ingress} && echo
```

## initialized this repo using start.spring.io

```sh
curl https://start.spring.io/starter.zip?type=maven-project \
  -d language=java \
  -d platformVersion=2.2.4.RELEASE \
  -d packaging=jar \
  -d jvmVersion=11 \
  -d groupId=com.springdeveloper \
  -d artifactId=hello-message \
  -d name=hello-message \
  -d description='Message function for riff' \
  -d packageName=functions \
  -d dependencies=web,cloud-function,actuator | tar -xzvf -  
```
