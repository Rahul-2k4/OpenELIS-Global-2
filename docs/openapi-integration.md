# OpenAPI (Swagger) Integration for OpenELIS Global

This document describes the OpenAPI (Swagger) integration added to OpenELIS Global for API documentation and testing.

## 🚀 Quick Start

### Development Environment

1. **Start the application in development mode:**
   ```bash
   mvn spring-boot:run -Dspring.profiles.active=dev
   # OR for traditional deployment
   mvn clean install
   # Deploy war file to Tomcat with dev profile
   ```

2. **Access Swagger UI:**
   ```
   http://localhost:8080/swagger-ui.html
   ```

3. **Access OpenAPI JSON specification:**
   ```
   http://localhost:8080/v3/api-docs
   ```

### Production Environment

**✅ SECURITY CONFIRMED:** OpenAPI is automatically disabled in production environments.

## 📋 API Groups

The OpenAPI documentation is organized into logical groups:

### 1. **OpenELIS Core REST API** (`/rest/**`)
- Patient management endpoints
- Sample tracking and management
- Test result operations
- Laboratory workflow APIs
- User and role management

### 2. **OpenELIS FHIR R4 API** (`/fhir/**`)
- FHIR R4 compliant endpoints
- Patient resource management
- Specimen and observation data
- Healthcare interoperability

### 3. **OpenELIS Common/Utility API** (`/common/**`, `/config/**`, `/util/**`)
- System configuration endpoints
- Common properties and utilities
- Application status and health checks

## 🔧 Configuration

### Application Properties

#### Development (`application-dev.properties`)
```properties
# Enable OpenAPI in development
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

#### Production (`application-prod.properties`)
```properties
# Disable OpenAPI in production (SECURITY)
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

### Environment-Specific Control

You can control OpenAPI availability using Spring profiles or environment variables:

```bash
# Enable for development
-Dspring.profiles.active=dev

# Disable for production
-Dspring.profiles.active=prod

# Override via environment variables
-Dspringdoc.api-docs.enabled=false
-Dspringdoc.swagger-ui.enabled=false
```

## 🛠️ Technical Implementation

### Dependencies Added

```xml
<!-- OpenAPI documentation support -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-webmvc-core</artifactId>
    <version>1.8.0</version>
</dependency>
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-ui</artifactId>
    <version>1.8.0</version>
</dependency>
```

### Configuration Class

- **Location:** `org.openelisglobal.config.OpenApiConfig`
- **Purpose:** Defines API groups and documentation metadata
- **Security:** Controlled by application properties

### Controller Scanning

The configuration automatically scans these packages for REST controllers:
- `org.openelisglobal.*.controller.rest`
- `org.openelisglobal.rest`
- `org.openelisglobal.common.rest`
- `org.openelisglobal.dataexchange.fhir.controller`
- `org.openelisglobal.fhir`

## 🔐 Security Considerations

### Production Safety
- **Disabled by default** in production environments
- **Property-controlled** enabling/disabling
- **Profile-based** configuration for different environments

### Access Control
- OpenAPI endpoints respect existing Spring Security configuration
- Authenticated access required for protected APIs
- No additional security vulnerabilities introduced

### Environment Separation
```properties
# Development: Full access to API documentation
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true

# Production: Complete lockdown
springdoc.api-docs.enabled=false
springdoc.swagger-ui.enabled=false
```

## 🧪 Testing the Integration

### 1. Verify Development Access
```bash
# Start application
mvn clean install
# Deploy to Tomcat with dev profile

# Test Swagger UI access
curl -I http://localhost:8080/swagger-ui.html
# Expected: 200 OK

# Test API docs access
curl http://localhost:8080/v3/api-docs | jq .
# Expected: Valid OpenAPI JSON
```

### 2. Verify Production Lockdown
```bash
# Start application with production profile
# Set: springdoc.api-docs.enabled=false

# Test Swagger UI blocked
curl -I http://localhost:8080/swagger-ui.html
# Expected: 404 Not Found

# Test API docs blocked
curl -I http://localhost:8080/v3/api-docs
# Expected: 404 Not Found
```

## 📚 Usage Examples

### Explore Available APIs
1. Navigate to `/swagger-ui.html`
2. Select an API group from the dropdown
3. Expand endpoints to view parameters and responses
4. Use "Try it out" to test endpoints directly

### Generate Client SDKs
```bash
# Download OpenAPI specification
curl http://localhost:8080/v3/api-docs > openapi.json

# Generate client SDK (example with OpenAPI Generator)
openapi-generator-cli generate -i openapi.json -g java -o ./client-sdk/
```

### API Testing
- Use Swagger UI's built-in testing interface
- Import OpenAPI spec into Postman
- Generate automated test suites from the specification

## 🔍 Troubleshooting

### Common Issues

#### 1. Swagger UI Not Loading
```bash
# Check if OpenAPI is enabled
grep -r "springdoc.api-docs.enabled" src/main/resources/

# Verify correct profile is active
# Check application logs for SpringDoc initialization
```

#### 2. No APIs Visible in Swagger UI
```bash
# Verify controller package scanning
# Check OpenApiConfig.java packagesToScan() configuration
# Ensure controllers have @RestController annotation
```

#### 3. Production Access (Security Issue)
```bash
# Immediately verify production properties
cat src/main/resources/application-prod.properties | grep springdoc

# Ensure application-prod.properties is loaded
# Check environment/profile configuration
```

### Debug Information

Enable debug logging for SpringDoc:
```properties
# Add to application-dev.properties
logging.level.org.springdoc=DEBUG
logging.level.org.openelisglobal.config.OpenApiConfig=DEBUG
```

## 🤝 Contributing

When adding new REST endpoints:

1. **Follow existing patterns** in controller organization
2. **Add proper OpenAPI annotations** for enhanced documentation
3. **Test in both development and production** environments
4. **Verify security** - ensure no sensitive endpoints are exposed

### Example Controller Annotation
```java
@RestController
@RequestMapping("/rest/patient")
@Tag(name = "Patient Management", description = "Patient registration and management operations")
public class PatientRestController {
    
    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID", description = "Retrieves patient information by unique identifier")
    @ApiResponse(responseCode = "200", description = "Patient found")
    @ApiResponse(responseCode = "404", description = "Patient not found")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable Long id) {
        // Implementation
    }
}
```

---

**🔒 SECURITY REMINDER:** Always verify OpenAPI is disabled in production environments before deployment.