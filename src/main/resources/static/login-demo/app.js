const statusMessage = document.getElementById("statusMessage");
const statusDetails = document.getElementById("statusDetails");
const loginForm = document.getElementById("loginForm");
const loginButton = document.getElementById("loginButton");
const meButton = document.getElementById("meButton");
const logoutButton = document.getElementById("logoutButton");

let token = sessionStorage.getItem("accessToken") || "";
let isLoading = false;
const REQUEST_TIMEOUT_MS = 10000;

function setSessionButtons() {
  const hasToken = Boolean(token);
  meButton.disabled = !hasToken || isLoading;
  logoutButton.disabled = !hasToken || isLoading;
}

function setStatus(type, text, details = []) {
  statusMessage.className = `status status-${type}`;
  statusMessage.textContent = text;

  statusDetails.innerHTML = "";
  details.forEach((detail) => {
    const item = document.createElement("li");
    item.textContent = detail;
    statusDetails.appendChild(item);
  });
}

function setLoading(loading) {
  isLoading = loading;
  loginButton.disabled = loading;
  loginButton.textContent = loading ? "Validando..." : "Iniciar sesion";
  setSessionButtons();
}

function readLoginError(errorBody, status) {
  if (status === 401) {
    return {
      message: "Credenciales invalidas. Verifica email y password.",
      details: [],
    };
  }

  if (status === 400) {
    const validationErrors = Object.values(errorBody.details || {});
    return {
      message: "Revisa la informacion del formulario.",
      details: validationErrors,
    };
  }

  return {
    message: errorBody.message || "No se pudo iniciar sesion por el momento.",
    details: [],
  };
}

async function fetchWithTimeout(url, options = {}) {
  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), REQUEST_TIMEOUT_MS);

  try {
    return await fetch(url, {
      ...options,
      signal: controller.signal,
    });
  } finally {
    clearTimeout(timeoutId);
  }
}

setSessionButtons();
setStatus(token ? "success" : "neutral", token ? "Sesion restaurada desde el navegador." : "Esperando inicio de sesion.");

loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();

  if (isLoading) {
    return;
  }

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  setLoading(true);
  setStatus("loading", "Validando credenciales...");

  try {
    const response = await fetchWithTimeout("/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      const errorBody = await response.json().catch(() => ({}));
      const readableError = readLoginError(errorBody, response.status);
      setStatus("error", readableError.message, readableError.details);
      return;
    }

    const data = await response.json();
    token = data.accessToken;
    sessionStorage.setItem("accessToken", token);
    setStatus("success", "Sesion iniciada correctamente.");
    setSessionButtons();
  } catch (error) {
    const message = error?.name === "AbortError"
      ? "Tiempo de espera agotado al conectar con el servidor."
      : "No se pudo conectar con el backend.";
    setStatus("error", message);
  } finally {
    setLoading(false);
  }
});

meButton.addEventListener("click", async () => {
  if (!token) {
    setStatus("error", "Inicia sesion para consultar tu perfil.");
    return;
  }

  setLoading(true);
  setStatus("loading", "Consultando perfil protegido...");

  try {
    const response = await fetchWithTimeout("/api/private/me", {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });

    const body = await response.json().catch(() => ({}));

    if (response.ok) {
      const details = [
        `Usuario: ${body.email || "No disponible"}`,
        `Roles: ${(body.roles || []).join(", ") || "Sin roles"}`,
      ];
      setStatus("success", "Perfil obtenido correctamente.", details);
      return;
    }

    if (response.status === 401) {
      token = "";
      sessionStorage.removeItem("accessToken");
      setSessionButtons();
      setStatus("error", "Tu sesion expiró. Inicia sesion nuevamente.");
      return;
    }

    setStatus("error", "No fue posible consultar el perfil en este momento.");
  } catch (error) {
    const message = error?.name === "AbortError"
      ? "Tiempo de espera agotado al consultar tu perfil."
      : "No se pudo conectar con el backend.";
    setStatus("error", message);
  } finally {
    setLoading(false);
  }
});

logoutButton.addEventListener("click", () => {
  token = "";
  sessionStorage.removeItem("accessToken");
  setSessionButtons();
  setStatus("neutral", "Sesion cerrada.");
});

// Registro - solo visual
const registerForm = document.getElementById("registerForm");
if (registerForm) {
  registerForm.addEventListener("submit", (event) => {
    event.preventDefault();

    const name = document.getElementById("register-name").value.trim();
    const email = document.getElementById("register-email").value.trim();

    // Mostrar éxito (solo visual)
    setStatus("success", "¡Cuenta creada exitosamente!", [
      `Nombre: ${name}`,
      `Email: ${email}`,
    ]);

    // Limpiar formulario
    registerForm.reset();
  });
}

