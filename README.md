# javalin-podman-template

Minimal Java 21 + Gradle 9.3.1 + Javalin 7 starter template.

## Quickstart

Windows PowerShell:

```powershell
.\scripts\bootstrap.ps1
```

Windows cmd:

```bat
scripts\bootstrap.bat
```

macOS/Linux:

```sh
chmod +x scripts/*.sh
./scripts/bootstrap.sh
```

Then run:

```powershell
.\gradlew.bat run
```

```sh
./gradlew run
```

## Common Commands

```powershell
.\gradlew.bat spotlessApply
.\gradlew.bat spotlessCheck test
.\scripts\dev-check.ps1
```

```bat
scripts\dev-check.bat
```

```sh
./scripts/dev-check.sh
```

## API

- `GET /health`
- `POST /api/v1/ping` with JSON body: `{"message":"hello"}`

All responses include `X-Request-Id`. You can pass your own request ID using that header.

## Podman

```powershell
.\scripts\container-up.ps1
```

```bat
scripts\container-up.bat
```

```sh
./scripts/container-up.sh
```

## Using This Template

See [TEMPLATE.md](TEMPLATE.md) for steps to rename package/project values after creating a new repo.
