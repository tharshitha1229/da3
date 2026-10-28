/**
 * AirTrack API Configuration & Dynamic URL Resolver
 * 
 * Determines whether to connect to local backend (http://localhost:8080/api)
 * or cloud backend based on environment, localStorage, or user settings.
 */

(function () {
    const LOCAL_DEFAULT = "http://localhost:8080/api";
    const RENDER_DEFAULT = "https://airline-backend-1lhg.onrender.com/api";

    function resolveApiUrl() {
        // 1. Explicit user override from localStorage
        const stored = localStorage.getItem("AIRTRACK_API_URL");
        if (stored && stored.trim()) {
            return stored.trim().replace(/\/+$/, "");
        }

        // 2. Global window override
        if (window.AIRTRACK_API_URL && window.AIRTRACK_API_URL.trim()) {
            return window.AIRTRACK_API_URL.trim().replace(/\/+$/, "");
        }

        // 3. Local environment detection (localhost, 127.0.0.1, or file://)
        const host = window.location.hostname;
        const proto = window.location.protocol;
        const isLocal = !host || host === "localhost" || host === "127.0.0.1" || host === "::1" || proto === "file:";

        if (isLocal) {
            return LOCAL_DEFAULT;
        }

        // 4. Render cloud environment detection
        if (host && host.includes("onrender.com")) {
            return RENDER_DEFAULT;
        }

        // Default to local development URL
        return LOCAL_DEFAULT;
    }

    const currentApiUrl = resolveApiUrl();

    // Export globally
    window.AIRTRACK_CONFIG = {
        LOCAL_DEFAULT: LOCAL_DEFAULT,
        RENDER_DEFAULT: RENDER_DEFAULT,
        getApiUrl: resolveApiUrl,
        setApiUrl: function (url) {
            if (url && url.trim()) {
                let cleanUrl = url.trim().replace(/\/+$/, "");
                if (!cleanUrl.endsWith("/api") && !cleanUrl.includes("/api/")) {
                    cleanUrl += "/api";
                }
                localStorage.setItem("AIRTRACK_API_URL", cleanUrl);
            } else {
                localStorage.removeItem("AIRTRACK_API_URL");
            }
            window.location.reload();
        },
        resetApiUrl: function () {
            localStorage.removeItem("AIRTRACK_API_URL");
            window.location.reload();
        },
        openApiSettingsModal: function () {
            let modal = document.getElementById("airtrack-api-settings-modal");
            if (!modal) {
                modal = document.createElement("div");
                modal.id = "airtrack-api-settings-modal";
                modal.innerHTML = `
                    <div style="position:fixed;inset:0;background:rgba(0,0,0,0.7);z-index:99999;display:flex;align-items:center;justify-content:center;backdrop-filter:blur(4px);font-family:system-ui,-apple-system,sans-serif;">
                        <div style="background:#1e293b;border:1px solid #334155;border-radius:12px;padding:24px;width:90%;max-width:480px;color:#f8fafc;box-shadow:0 20px 25px -5px rgba(0,0,0,0.5);">
                            <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:16px;">
                                <h3 style="margin:0;font-size:18px;font-weight:600;display:flex;align-items:center;gap:8px;">
                                    <span>⚙️</span> API Connection Settings
                                </h3>
                                <button onclick="document.getElementById('airtrack-api-settings-modal').remove()" style="background:none;border:none;color:#94a3b8;font-size:20px;cursor:pointer;padding:4px;">✕</button>
                            </div>
                            <p style="margin:0 0 16px 0;font-size:13px;color:#94a3b8;line-height:1.5;">
                                Configure the backend API endpoint. For local development on your laptop, use <strong>http://localhost:8080/api</strong>.
                            </p>
                            <label style="display:block;font-size:12px;font-weight:600;color:#cbd5e1;margin-bottom:6px;">API BASE URL</label>
                            <input id="airtrack-custom-api-input" type="text" value="${window.AIRTRACK_CONFIG.getApiUrl()}" style="width:100%;box-sizing:border-box;padding:10px 12px;background:#0f172a;border:1px solid #475569;border-radius:6px;color:#f8fafc;font-size:14px;margin-bottom:16px;outline:none;" />
                            
                            <div style="display:flex;gap:8px;margin-bottom:20px;">
                                <button type="button" onclick="document.getElementById('airtrack-custom-api-input').value='${LOCAL_DEFAULT}'" style="flex:1;padding:8px 10px;background:#334155;border:1px solid #475569;border-radius:6px;color:#f8fafc;font-size:12px;cursor:pointer;font-weight:500;">Use Localhost:8080</button>
                                <button type="button" onclick="document.getElementById('airtrack-custom-api-input').value='${RENDER_DEFAULT}'" style="flex:1;padding:8px 10px;background:#334155;border:1px solid #475569;border-radius:6px;color:#f8fafc;font-size:12px;cursor:pointer;font-weight:500;">Use Render Cloud</button>
                            </div>

                            <div id="airtrack-test-result" style="display:none;padding:10px;border-radius:6px;font-size:13px;margin-bottom:16px;"></div>

                            <div style="display:flex;justify-content:space-between;align-items:center;">
                                <button type="button" id="airtrack-test-btn" style="padding:8px 14px;background:#0284c7;border:none;border-radius:6px;color:#fff;font-size:13px;cursor:pointer;font-weight:500;">Test Connection</button>
                                <div style="display:flex;gap:8px;">
                                    <button type="button" onclick="window.AIRTRACK_CONFIG.resetApiUrl()" style="padding:8px 14px;background:#475569;border:none;border-radius:6px;color:#fff;font-size:13px;cursor:pointer;">Reset Default</button>
                                    <button type="button" id="airtrack-save-btn" style="padding:8px 16px;background:#16a34a;border:none;border-radius:6px;color:#fff;font-size:13px;cursor:pointer;font-weight:600;">Save & Reload</button>
                                </div>
                            </div>
                        </div>
                    </div>
                `;
                document.body.appendChild(modal);

                document.getElementById('airtrack-save-btn').onclick = function () {
                    const inputVal = document.getElementById('airtrack-custom-api-input').value;
                    window.AIRTRACK_CONFIG.setApiUrl(inputVal);
                };

                document.getElementById('airtrack-test-btn').onclick = async function () {
                    const inputVal = document.getElementById('airtrack-custom-api-input').value.trim().replace(/\/+$/, "");
                    const testUrl = (inputVal.endsWith("/api") ? inputVal : inputVal + "/api") + "/health";
                    const resultDiv = document.getElementById('airtrack-test-result');
                    resultDiv.style.display = "block";
                    resultDiv.style.background = "rgba(59, 130, 246, 0.15)";
                    resultDiv.style.color = "#60a5fa";
                    resultDiv.style.border = "1px solid #3b82f6";
                    resultDiv.textContent = "Testing connection to " + testUrl + "...";

                    try {
                        const controller = new AbortController();
                        const timer = setTimeout(() => controller.abort(), 6000);
                        const res = await fetch(testUrl, { signal: controller.signal });
                        clearTimeout(timer);
                        if (res.ok) {
                            const json = await res.json();
                            resultDiv.style.background = "rgba(34, 197, 94, 0.15)";
                            resultDiv.style.color = "#4ade80";
                            resultDiv.style.border = "1px solid #22c55e";
                            resultDiv.textContent = "✓ Connected successfully! Server status: " + (json.status || "UP");
                        } else {
                            resultDiv.style.background = "rgba(239, 68, 68, 0.15)";
                            resultDiv.style.color = "#f87171";
                            resultDiv.style.border = "1px solid #ef4444";
                            resultDiv.textContent = "✗ HTTP Error " + res.status + " from " + testUrl;
                        }
                    } catch (e) {
                        resultDiv.style.background = "rgba(239, 68, 68, 0.15)";
                        resultDiv.style.color = "#f87171";
                        resultDiv.style.border = "1px solid #ef4444";
                        resultDiv.textContent = "✗ Connection failed: " + (e.message || "Network error. Is the backend running?");
                    }
                };
            }
        }
    };

    // Backward-compatibility
    window.AIRTRACK_API_URL = currentApiUrl;
    window.setApiUrl = window.AIRTRACK_CONFIG.setApiUrl;
})();
