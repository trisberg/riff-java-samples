# hello-boot-fun-webflux

## Build

```
riff function create hello-boot-fun-webflux-pojo --handler hello \
  --git-repo https://github.com/trisberg/riff-java-samples.git \
  --sub-path hello-boot-fun-webflux-pojo \
  --tail
```

## Deploy

```
riff core deployer create hello --function-ref hello-boot-fun-webflux-pojo \
  --ingress-policy External \
  --env spring_main_web_application_type=NONE \
  --tail
```

## Invoke

```
ingress=$(kubectl get svc/nginx-ingress-controller -n nginx-ingress -ojsonpath='{.status.loadBalancer.ingress[0].ip}')
curl ${ingress} -H 'Host: hello.default.example.com' -H 'Content-Type: application/json' -H 'Accept: application/json' -d '{"type": "test", "name": "riff"}' && echo
```

## Teardown

```
riff core deployer delete hello
riff function delete hello-boot-fun-webflux-pojo
```

## Local test

```
./mvnw spring-boot:run
curl localhost:8080 -H 'Content-Type: application/json' -H 'Accept: application/json' -d '{"type": "test", "name": "riff"}' && echo
```
