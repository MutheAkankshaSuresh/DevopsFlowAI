const API_BASE = import.meta.env.VITE_API_BASE || "http://localhost:8080";

export async function api(path, options = {}) {
  const token = localStorage.getItem("devopsflow_token");
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {})
    }
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: response.statusText }));
    throw new Error(error.message || "Request failed");
  }

  return response.status === 204 ? null : response.json();
}

export { API_BASE };
