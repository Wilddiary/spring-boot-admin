<h1 align="center" style="font-weight: bold; margin-top: 20px; margin-bottom: 20px;">Spring Boot Admin</h1>

<p align="center">

  <img alt="Github Build" src="https://img.shields.io/github/actions/workflow/status/Wilddiary/spring-boot-admin/maven-build.yml" />
  <img alt="Synk Vulnerabilities" src="https://img.shields.io/snyk/vulnerabilities/github/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Language Count" src="https://img.shields.io/github/languages/count/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Top Language" src="https://img.shields.io/github/languages/top/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Repo Size" src="https://img.shields.io/github/repo-size/Wilddiary/spring-boot-admin" />
  <img alt="GitHub File Count" src="https://img.shields.io/github/directory-file-count/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Issues" src="https://img.shields.io/github/issues/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Closed Issues" src="https://img.shields.io/github/issues-closed/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Pull Requests" src="https://img.shields.io/github/issues-pr/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Closed Pull Requests" src="https://img.shields.io/github/issues-pr-closed/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Release" src="https://img.shields.io/github/v/release/Wilddiary/spring-boot-admin?date_order_by=created_at&sort=date" />
  <img alt="GitHub Tag" src="https://img.shields.io/github/v/tag/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Contributors" src="https://img.shields.io/github/contributors/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Last Commit" src="https://img.shields.io/github/last-commit/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Commit Activity (Week)" src="https://img.shields.io/github/commit-activity/w/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Commit Activity (Month)" src="https://img.shields.io/github/commit-activity/m/Wilddiary/spring-boot-admin" />
  <img alt="GitHub Commit Activity (Year)" src="https://img.shields.io/github/commit-activity/y/Wilddiary/spring-boot-admin" />
  <img alt="Github License" src="https://img.shields.io/github/license/Wilddiary/spring-boot-admin" />
  <img alt="Forks" src="https://img.shields.io/github/forks/Wilddiary/spring-boot-admin" />
  <img alt="Followers" src="https://img.shields.io/github/followers/Wilddiary" />
  <img alt="Discussions" src="https://img.shields.io/github/discussions/Wilddiary/spring-boot-admin" />

</p>


The repository hosts a Spring Boot Admin server capable of the discovering services in Kubernetes environment when provided with appropriate permissions. The admin probes the services through their actuator interface. The monitored services should use the Spring Boot Admin client by including its dependency in their pom.xml to register themselves with the Admin server to allow additional details for better monitoring.   

# Build
> mvn clean package

# Build Docker Image
> mvn k8s:build
>
Building the application jar is a pre-requisite for building the docker image.

# Deploying to Kubernetes
> mvn k8s:resource k8s:apply
>
Building the docker image is a pre-requisite for deploy.

# HTTP interface
The application exposes the Spring Boot Admin dashboard on the default HTTP port `10000`. The default login credentials are `admin/admin`.

# Screenshots
![login.png](docs/images/login.png)
![wallboard.png](docs/images/wallboard.png)
![details.png](docs/images/details.png)
![jvm-threaddump.png](docs/images/jvm-threaddump.png)
![loggers.png](docs/images/loggers.png)
