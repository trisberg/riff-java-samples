# hello-boot-fun-web-pojo

## Build

```
riff function create hello-boot-fun-web-pojo --handler hello \
  --git-repo https://github.com/trisberg/riff-java-samples.git \
  --sub-path hello-boot-fun-web-pojo \
  --tail
```

## Deploy

```
riff knative deployer create hello --function-ref hello-boot-fun-web-pojo \
  --ingress-policy External \
  --env spring_main_web_application_type=NONE \
  --tail
```

## Invoke

```
ingress=$(kubectl get svc envoy-external --namespace projectcontour --output 'jsonpath={.status.loadBalancer.ingress[0].ip}')
curl ${ingress} -H 'Host: hello.default.example.com' -H 'Content-Type: application/json' -H 'Accept: text/plain' -d '{"type": "test", "name": "riff"}' && echo
```

## Teardown

```
riff knative deployer delete hello
riff function delete hello-boot-fun-web-pojo
```

## Local test

```
./mvnw spring-boot:run
curl localhost:8080 -H 'Content-Type: application/json' -H 'Accept: text/plain' -d '{"type": "test", "name": "riff"}' && echo
```
