# SentinelAPI: A Secure, Observable Backend Platform 🛡️

SentinelAPI is a production-style HTTP backend engineered in Java. It is designed to demonstrate the critical intersection of **Software Engineering** and **Cybersecurity** by implementing industry-standard protection layers and observability patterns.

---

## 🎯 Project Purpose & "The Why"
In a real-world production environment, a server that just "works" is a liability. It must be **Secure** (to prevent unauthorized access), **Stable** (to survive traffic spikes), and **Observable** (to allow engineers to audit what happened).

I built SentinelAPI to bridge the gap between "simple coding" and "systems engineering." This project mimics the infrastructure used by major tech companies to protect their internal APIs from common web vulnerabilities.

---

## 🌍 Real-World Applications
SentinelAPI addresses several critical IT and Security challenges:
* **Mitigating DoS Attacks:** By implementing Rate Limiting at the entry point.
* **Access Control:** Enforcing strict API Key authentication (similar to AWS or Twilio).
* **Compliance & Auditing:** Providing structured logs required for SOC2 or HIPAA compliance standards.
* **Environment Parity:** Using Docker to ensure the "Security Posture" remains identical from development to production.

---

## ✨ Key Features

### 1. Security Middleware (The Gatekeeper)
Every request passes through a custom filter chain before reaching the server logic.
* **Authentication:** Validates `X-API-KEY` headers.
* **Security Header Injection:** Injects `X-Content-Type-Options: nosniff` and `X-Frame-Options` to prevent clickjacking and MIME-sniffing attacks.

### 2. Adaptive Rate Limiting
Prevents resource exhaustion by tracking IP addresses in a thread-safe `ConcurrentHashMap`.
* **Threshold:** 5 requests per window.
* **HTTP 429:** Returns a "Too Many Requests" status code once the threshold is crossed.

### 3. Structured Observability
A centralized logging utility that records:
* **Timestamps & IP Addresses**
* **HTTP Methods & URIs**
* **Final Status Codes (200, 401, 429)**

### 4. Containerized Architecture
Packaged with Docker for platform independence, supporting both **Intel/AMD** and **Apple Silicon (ARM64)** architectures.

---

## 🛠️ Technical Stack
* **Language:** Java 23 (JDK 23)
* **Runtime:** Eclipse Temurin (Ubuntu-based Jammy)
* **Tools:** Docker Desktop
* **Networking:** Java `com.sun.net.httpserver`
* **Concurrency:** Java `AtomicInteger` & `ConcurrentHashMap`

---

## 🚦 Getting Started

### Prerequisites
* Docker Desktop installed.
* Java 23 installed (for local compilation).

### Installation & Deployment
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/YOUR_USERNAME/SentinelAPI.git](https://github.com/YOUR_USERNAME/SentinelAPI.git)
   cd SentinelAPI

## Compile the Source:
javac -d out src/main/java/com/sentinel/api/**/*.java

## Build the Docker Container:
docker build -t sentinel-api .

## Run the Platform:
docker run -d -p 8080:8080 --name sentinel-server sentinel-api

### Testing the Security
## Authorized Request:
curl -i -H "X-API-KEY: sentinel-secret-2026" http://localhost:8080

## Blocked Request (Invalid Key):
curl -i http://localhost:8080
📈 Future Roadmap
[ ] Persistent Logging via Docker Volumes.
[ ] Transition to JWT (JSON Web Token) Authentication.
[ ] Integration with a SQL database for dynamic user management.

Created by Miguel Adrienne Corachea - Focused on Secure Infrastructure and Backend Resilience.


---

### Step 3: Pushing to GitHub
If you have Git installed, run these commands in order:

1.  **Initialize:** `git init`
2.  **Add all files:** `git add .` (This will include your `.gitignore` so your junk files stay out!)
3.  **Commit:** `git commit -m "Initial Release: Secure and Observable Backend"`
4.  **Create Repo:** Go to GitHub.com, create a new repo named `SentinelAPI`, then follow the instructions they show to "push an existing repository."