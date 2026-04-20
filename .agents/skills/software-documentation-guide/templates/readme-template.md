---
description: |
  README template for software projects. Copy to project root and customize.
  Use when: creating a README for a new or existing project.
---

# [Project Name]

> One-line description of what this project does.

## What

[2-3 sentences explaining the project's purpose and who it's for.]

## Why

[What problem does this solve? What was the alternative before this project?]

## Quick Start

### Prerequisites

- [Language] [version] (e.g., Java 17, Node 20, Python 3.11)
- [Build tool] (e.g., Maven 3.9, npm, pip)
- [Database] (if applicable)

### Install

```bash
# Clone
git clone https://github.com/[org]/[repo].git
cd [repo]

# Install dependencies
mvn install -DskipTests    # Java
npm install                 # Node
pip install -r requirements.txt  # Python
```

### Run

```bash
mvn spring-boot:run        # Java/Spring
npm start                   # Node
python -m app               # Python
```

### Test

```bash
mvn verify                  # Java (tests + coverage)
npm test                    # Node
pytest --cov               # Python
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── model/          ← Domain entities
│   │   ├── dao/            ← Data access
│   │   ├── service/        ← Business logic
│   │   └── controller/     ← REST endpoints
│   └── resources/
│       └── application.yml ← Configuration
└── test/
    └── java/com/example/   ← Tests mirror main structure
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/[resource]` | List all |
| GET | `/api/[resource]/{id}` | Get by ID |
| POST | `/api/[resource]` | Create |
| PUT | `/api/[resource]/{id}` | Update |
| DELETE | `/api/[resource]/{id}` | Delete |

## Configuration

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:h2:mem:test` | Database connection URL |
| `DB_USER` | `sa` | Database username |
| `SERVER_PORT` | `8080` | Application port |

## Contributing

1. Create a feature branch: `git checkout -b feat/my-feature`
2. Write tests first, then implementation
3. Ensure `mvn verify` passes
4. Submit a pull request

## License

[MIT / Apache 2.0 / Proprietary]
