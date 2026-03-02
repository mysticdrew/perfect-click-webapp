![Docker Image Version (tag)](https://img.shields.io/docker/v/mysticdrew/config-server/latest)

# config-server

This is a config store for applications to pull full configs, as properties files or as json paylods.

Configs can be stored in a file(default) on your mounted filepath or sql.

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
- `GET /api/v1/config` list config files and field counts
- `POST /api/v1/config` with JSON body: `{"name":"application-a"}`
- `GET /api/v1/config/{name}/json` full config as JSON for the admin UI
- `GET /api/v1/config/{name}` properties text for consumer applications
  - Optional query param: `?key=field.name` to return only a single field value
- `PUT /api/v1/config/{name}/fields/{key}` with JSON body:
  `{"type":"STRING|INTEGER|LONG|BOOLEAN","value":"raw-value"}`
- `DELETE /api/v1/config/{name}/fields/{key}`
- `DELETE /api/v1/config/{name}`

All responses include `X-Request-Id`. You can pass your own request ID using that header.

## Frontend

The web UI is served from `/` and lets you:

- Create/delete `.properties` files
- Add/edit/delete typed fields
- Copy the consumer endpoint for each config

## Environment

- `PORT` (default `7000`)
- `APP_ENV` (default `dev`)
- `APP_CONFIG_STORE` (default `file`, options: `file`, `sqlserver`)
- `APP_CONFIG_DIR` (default `data/configs`; used when `APP_CONFIG_STORE=file`)
- `APP_SQLSERVER_JDBC_URL` (required when `APP_CONFIG_STORE=sqlserver`)
- `APP_SQLSERVER_USERNAME` (optional)
- `APP_SQLSERVER_PASSWORD` (optional)

### Storage modes

- File mode (`APP_CONFIG_STORE=file`) writes `.properties` + type metadata files to `APP_CONFIG_DIR`.
- SQL Server mode (`APP_CONFIG_STORE=sqlserver`) stores configs and fields in database tables.
- In containers, keep file mode data outside the image via a mounted volume (see Podman section).

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

`podman-compose.yml` now mounts a named volume to `/data/configs` and passes the storage env vars so file data survives container rebuilds/restarts.
