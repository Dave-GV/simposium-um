const output = document.getElementById("output");
const loginForm = document.getElementById("loginForm");
const meButton = document.getElementById("meButton");
const logoutButton = document.getElementById("logoutButton");

let token = sessionStorage.getItem("accessToken") || "";

function render(data) {
  output.textContent = JSON.stringify(data, null, 2);
}

if (token) {
  render({ status: "ok", note: "Sesion restaurada desde navegador" });
}

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  render({ status: "loading", action: "POST /auth/login" });

  // Evita que la UI se quede colgada si el backend no responde.
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), 10000);

  try {
    const response = await fetch("/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
      signal: controller.signal,
    });

    if (!response.ok) {
      render({ status: response.status, error: "Credenciales invalidas" });
      return;
    }

    const data = await response.json();
    token = data.accessToken;
    sessionStorage.setItem("accessToken", token);
    render({ status: response.status, user: data.email, note: "Sesion iniciada" });
  } catch (error) {
    const detail = error?.name === "AbortError"
      ? "Timeout al conectar con /auth/login"
      : "No se pudo conectar al backend";
    render({ status: "error", error: detail });
  } finally {
    clearTimeout(timeoutId);
  }
});

meButton.addEventListener("click", async () => {
  if (!token) {
    render({ error: "Primero inicia sesion para obtener token" });
    return;
  }

  render({ status: "loading", action: "GET /api/private/me" });

  const response = await fetch("/api/private/me", {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const body = await response.json().catch(() => ({}));
  render({ status: response.status, body });
});

logoutButton.addEventListener("click", () => {
  token = "";
  sessionStorage.removeItem("accessToken");
  render({ status: "ok", note: "Sesion cerrada" });
});

