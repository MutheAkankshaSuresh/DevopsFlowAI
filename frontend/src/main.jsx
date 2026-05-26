import React, { useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import { Activity, AlertTriangle, Boxes, CheckCircle2, GitBranch, Lock, Rocket, Search, Server, Sparkles, Timer, Users } from "lucide-react";
import { Bar, BarChart, CartesianGrid, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts";
import { api, API_BASE } from "./api/client";
import "./styles.css";

const colors = ["#1f8a70", "#d95f02", "#5e3c99", "#2c7fb8", "#b2182b"];

function Login({ onLogin }) {
  const [email, setEmail] = useState("admin@devopsflow.ai");
  const [password, setPassword] = useState("admin123");
  const [error, setError] = useState("");

  async function submit(event) {
    event.preventDefault();
    setError("");
    try {
      const auth = await api("/api/auth/login", { method: "POST", body: JSON.stringify({ email, password }) });
      localStorage.setItem("devopsflow_token", auth.token);
      onLogin(auth);
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <main className="login-shell">
      <section className="login-panel">
        <div>
          <p className="eyebrow">Enterprise DevOps Portfolio Project</p>
          <h1>DevOpsFlow AI</h1>
          <p className="muted">Intelligent task, deployment and API monitoring for engineering teams.</p>
        </div>
        <form onSubmit={submit} className="login-form">
          <label>Email<input value={email} onChange={(event) => setEmail(event.target.value)} /></label>
          <label>Password<input type="password" value={password} onChange={(event) => setPassword(event.target.value)} /></label>
          {error && <p className="error">{error}</p>}
          <button type="submit"><Lock size={18} /> Sign in</button>
        </form>
      </section>
      <section className="signal-grid">
        <div><Rocket /><span>Deployment Risk</span></div>
        <div><Activity /><span>API Uptime</span></div>
        <div><Sparkles /><span>AI Insights</span></div>
      </section>
    </main>
  );
}

function Metric({ icon: Icon, label, value, tone }) {
  return (
    <article className={`metric ${tone || ""}`}>
      <Icon size={22} />
      <div>
        <span>{label}</span>
        <strong>{value}</strong>
      </div>
    </article>
  );
}

function StatusPill({ value }) {
  return <span className={`pill ${String(value).toLowerCase()}`}>{value}</span>;
}

function App() {
  const [auth, setAuth] = useState(() => localStorage.getItem("devopsflow_token") ? { name: "Demo User" } : null);
  const [dashboard, setDashboard] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [deployments, setDeployments] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [logs, setLogs] = useState([]);
  const [query, setQuery] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (!auth) return;
    refresh();
  }, [auth]);

  async function refresh() {
    try {
      setError("");
      const [summary, taskPage, deploymentPage, alertList, apiLogs] = await Promise.all([
        api("/api/dashboard"),
        api("/api/tasks?size=20&sort=updatedAt,desc"),
        api("/api/deployments?size=20&sort=deployedAt,desc"),
        api("/api/alerts"),
        api("/api/monitoring/logs")
      ]);
      setDashboard(summary);
      setTasks(taskPage.content || []);
      setDeployments(deploymentPage.content || []);
      setAlerts(alertList);
      setLogs(apiLogs);
    } catch (err) {
      setError(err.message);
    }
  }

  async function addDemoDeployment() {
    await api("/api/deployments", {
      method: "POST",
      body: JSON.stringify({
        version: `v${new Date().getHours()}.${new Date().getMinutes()}.demo`,
        environment: "staging",
        status: "FAILED",
        deployedById: 1,
        releaseNotes: "Urgent auth hotfix with database migration"
      })
    });
    refresh();
  }

  const taskChart = useMemo(() => Object.entries(dashboard?.taskStatus || {}).map(([name, value]) => ({ name, value })), [dashboard]);
  const deploymentChart = useMemo(() => Object.entries(dashboard?.deploymentStatus || {}).map(([name, value]) => ({ name, value })), [dashboard]);
  const filteredTasks = tasks.filter((task) => `${task.title} ${task.description}`.toLowerCase().includes(query.toLowerCase()));

  if (!auth) return <Login onLogin={setAuth} />;

  return (
    <main className="app-shell">
      <aside className="sidebar">
        <div className="brand"><Boxes size={26} /><span>DevOpsFlow AI</span></div>
        <nav>
          <a className="active"><Activity size={18} /> Dashboard</a>
          <a><GitBranch size={18} /> Deployments</a>
          <a><Server size={18} /> API Monitor</a>
          <a><Users size={18} /> Team Flow</a>
        </nav>
        <button className="ghost" onClick={() => { localStorage.removeItem("devopsflow_token"); setAuth(null); }}>Logout</button>
      </aside>

      <section className="workspace">
        <header className="topbar">
          <div>
            <p className="eyebrow">Operational Command Center</p>
            <h1>Engineering delivery health</h1>
          </div>
          <button onClick={addDemoDeployment}><Rocket size={18} /> Simulate risky deploy</button>
        </header>

        {error && <div className="banner">{error}. Backend expected at {API_BASE}</div>}

        <section className="metrics-row">
          <Metric icon={CheckCircle2} label="Completed Tasks" value={dashboard?.completedTasks ?? "-"} tone="good" />
          <Metric icon={Rocket} label="Deploy Success" value={`${dashboard?.deploymentSuccessRate ?? "-"}%`} />
          <Metric icon={Timer} label="API Uptime" value={`${dashboard?.apiUptime ?? "-"}%`} tone="good" />
          <Metric icon={AlertTriangle} label="Open Alerts" value={dashboard?.openAlerts ?? "-"} tone="warn" />
        </section>

        <section className="insight-band">
          <Sparkles size={22} />
          <p>{dashboard?.aiInsight || "Loading intelligent recommendation..."}</p>
        </section>

        <section className="dashboard-grid">
          <div className="panel">
            <div className="panel-title"><h2>Task Pipeline</h2><div className="search"><Search size={16} /><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search tasks" /></div></div>
            <div className="kanban">
              {["PENDING", "IN_PROGRESS", "TESTING", "COMPLETED", "FAILED"].map((status) => (
                <div className="lane" key={status}>
                  <h3>{status.replace("_", " ")}</h3>
                  {filteredTasks.filter((task) => task.status === status).map((task) => (
                    <article className="task" key={task.id}>
                      <strong>{task.title}</strong>
                      <p>{task.description}</p>
                      <footer><StatusPill value={task.priority} /><span>{task.deadline}</span></footer>
                    </article>
                  ))}
                </div>
              ))}
            </div>
          </div>

          <div className="panel analytics">
            <h2>Analytics</h2>
            <div className="chart-box">
              <ResponsiveContainer width="100%" height={180}>
                <BarChart data={taskChart}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" hide />
                  <YAxis allowDecimals={false} />
                  <Tooltip />
                  <Bar dataKey="value" fill="#1f8a70" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>
            <div className="chart-box">
              <ResponsiveContainer width="100%" height={180}>
                <PieChart>
                  <Pie data={deploymentChart} dataKey="value" nameKey="name" outerRadius={70}>
                    {deploymentChart.map((entry, index) => <Cell key={entry.name} fill={colors[index % colors.length]} />)}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>
        </section>

        <section className="lower-grid">
          <div className="panel">
            <h2>Deployment Timeline</h2>
            <div className="table-list">
              {deployments.map((deployment) => (
                <article className="row-item" key={deployment.id}>
                  <div><strong>{deployment.version}</strong><span>{deployment.environment}</span></div>
                  <StatusPill value={deployment.status} />
                  <span className="risk">Risk {deployment.riskScore}</span>
                </article>
              ))}
            </div>
          </div>
          <div className="panel">
            <h2>Alerts & API Logs</h2>
            <div className="table-list">
              {alerts.slice(0, 3).map((alert) => (
                <article className="row-item" key={`a-${alert.id}`}>
                  <div><strong>{alert.type}</strong><span>{alert.message}</span></div>
                  <StatusPill value={alert.severity} />
                </article>
              ))}
              {logs.slice(0, 2).map((log) => (
                <article className="row-item" key={`l-${log.id}`}>
                  <div><strong>{log.endpoint?.name}</strong><span>{log.responseTimeMs}ms response</span></div>
                  <StatusPill value={log.up ? "UP" : "DOWN"} />
                </article>
              ))}
            </div>
          </div>
        </section>
      </section>
    </main>
  );
}

createRoot(document.getElementById("root")).render(<App />);
