
## Create Docker container 

```bash
docker run -d --name consul -p 8500:8500 \
  --add-host=host.docker.internal:host-gateway \
  hashicorp/consul:1.17 agent -dev -client=0.0.0.0
```