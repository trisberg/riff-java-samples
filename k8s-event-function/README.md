# k8s-event-function

K8s event processing sample function for riff

## build

```sh
riff application create k8s-event-fun \
  --git-repo https://github.com/trisberg/riff-java-samples.git \
  --sub-path k8s-event-function \
  --tail
```

## deploy

```sh
riff knative deployer create k8s-event-fun \
  --application-ref k8s-event-fun \
  --ingress-policy External \
  --min-scale 1 \
  --tail
```

## invoke

```sh
ingress=$(kubectl get svc envoy-external --namespace projectcontour --output 'jsonpath={.status.loadBalancer.ingress[0].ip}')
curl -X POST \
  -H 'Host: k8s-event-fun.default.example.com' \
  -H 'Content-Type: application/json' \
  -H 'Accept: text/plain' \
  -H "ce-specversion: 1.0" \
  -H "ce-source: curl-command" \
  -H "ce-type: curl.demo" \
  -H "ce-id: 123-abc" \
  -d '"test"' \
  ${ingress} && echo
```
