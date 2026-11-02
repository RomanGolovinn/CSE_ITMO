const API_URL = "/api/point";
const canvas = document.getElementById("coordinateCanvas");
const ctx = canvas.getContext("2d");
const form = document.getElementById("pointForm");
const xInputs = Array.from(document.querySelectorAll('input[name="x"]'));
const yInput = document.getElementById("y");
const rInputs = Array.from(document.querySelectorAll('input[name="r"]'));
const xError = document.getElementById("xError");
const yError = document.getElementById("yError");
const rError = document.getElementById("rError");
const formMessage = document.getElementById("formMessage");
const resultsBody = document.getElementById("resultsBody");
const clearResultsButton = document.getElementById("clearResults");

let results = [];

function parseNumber(value) {
    const normalized = value.trim().replace(",", ".");
    if (normalized === "" || /\.\d{11,}/.test(normalized)) return null;
    const number = Number(normalized);
    return Number.isFinite(number) ? number : null;
}

function clearErrors() {
    xError.textContent = "";
    yError.textContent = "";
    rError.textContent = "";
    formMessage.textContent = "";
    formMessage.className = "form-message";
}

function validateForm() {
    clearErrors();
    const xs = xInputs
        .filter(function(input) { return input.checked; })
        .map(function(input) { return Number(input.value); });
    const y = parseNumber(yInput.value);
    const selectedR = document.querySelector('input[name="r"]:checked');
    const r = selectedR ? Number(selectedR.value) : null;
    let valid = true;

    if (xs.length === 0) {
        xError.textContent = "Выберите хотя бы одно значение X.";
        valid = false;
    }

    if (y === null) {
        yError.textContent = "Неверный формат.";
        valid = false;
    } else if (y < -5 || y > 5) {
        yError.textContent = "Y должен быть от -5 до 5 включительно.";
        valid = false;
    }

    if (![1, 1.5, 2, 2.5, 3].includes(r)) {
        rError.textContent = "Выберите R: 1, 1.5, 2, 2.5 или 3.";
        valid = false;
    }
    return { valid, xs, y, r };
}

function formatDate(timestamp) {
    return new Intl.DateTimeFormat("ru-RU", {
        dateStyle: "short",
        timeStyle: "medium"
    }).format(new Date(timestamp));
}

function renderResults() {
    resultsBody.replaceChildren();
    if (results.length === 0) {
        const row = document.createElement("tr");
        row.className = "empty-row";
        const cell = document.createElement("td");
        cell.colSpan = 5;
        cell.textContent = "История проверок пуста.";
        row.appendChild(cell);
        resultsBody.appendChild(row);
        return;
    }

    results.forEach(function(result) {
        const row = document.createElement("tr");
        [result.x, result.y, result.r].forEach(function(value) {
            const cell = document.createElement("td");
            cell.textContent = String(value);
            row.appendChild(cell);
        });

        const outcomeCell = document.createElement("td");
        const outcome = document.createElement("span");
        outcome.className = result.hit ? "result-hit" : "result-miss";
        outcome.textContent = result.hit ? "Попадание" : "Промах";
        outcomeCell.appendChild(outcome);
        row.appendChild(outcomeCell);

        const timeCell = document.createElement("td");
        timeCell.textContent = formatDate(result.timestamp);
        row.appendChild(timeCell);
        resultsBody.appendChild(row);
    });
}

async function sendRequest(parameters) {
    const response = await fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
        },
        body: new URLSearchParams(parameters)
    });
    return { status: response.status, payload: await response.json() };
}

function applyServerResponse(payload) {
    results = Array.isArray(payload.results) ? payload.results : [];
    renderResults();
    drawCanvas();
}

function showError(message) {
    formMessage.textContent = message;
    formMessage.className = "form-message error";
}

form.addEventListener("submit", async function(event) {
    event.preventDefault();
    const validation = validateForm();
    if (!validation.valid) {
        formMessage.textContent = "Исправьте ошибки в форме.";
        formMessage.className = "form-message error";
        return;
    }

    formMessage.textContent = "Отправка запросов…";
    formMessage.className = "form-message";
    try {
        const checkedPoints = [];
        let lastPayload = null;
        for (const x of validation.xs) {
            const response = await sendRequest({
                action: "check",
                x: String(x),
                y: String(validation.y),
                r: String(validation.r)
            });
            lastPayload = response.payload;
            applyServerResponse(lastPayload);
            if (!response.status.toString().startsWith("2") || lastPayload.error) {
                showError(lastPayload.error || "Ошибка сервера: " + response.status);
                return;
            }
            if (Array.isArray(lastPayload.results) && lastPayload.results.length > 0) {
                checkedPoints.push(lastPayload.results[0]);
            }
        }

        const hits = checkedPoints.filter(function(point) { return point.hit; }).length;
        formMessage.textContent = "Проверено точек: " + checkedPoints.length
            + "; попаданий: " + hits + ". Серверное время: "
            + formatDate(lastPayload.currentTime) + "; время последнего запроса: "
            + Number(lastPayload.executionTimeMs).toFixed(3) + " мс.";
        formMessage.className = hits === checkedPoints.length
            ? "form-message success"
            : "form-message error";
    } catch (error) {
        showError("Не удалось связаться с сервером: " + error.message);
    }
});

clearResultsButton.addEventListener("click", async function() {
    try {
        const response = await sendRequest({ action: "clear" });
        applyServerResponse(response.payload);
        if (response.payload.error) {
            showError(response.payload.error);
            return;
        }
        formMessage.textContent = "История проверок очищена на сервере.";
        formMessage.className = "form-message success";
    } catch (error) {
        showError("Не удалось очистить историю: " + error.message);
    }
});

rInputs.forEach(function(input) {
    input.addEventListener("change", drawCanvas);
});

function drawCanvas() {
    const originX = 340;
    const originY = 250;
    const scale = 44;
    const selectedR = document.querySelector('input[name="r"]:checked');
    const r = selectedR ? Number(selectedR.value) : 1;
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    drawRegion(originX, originY, scale, r);
    drawAxes(originX, originY, scale, r);
    drawSavedPoints(originX, originY, scale);
}

function drawRegion(originX, originY, scale, r) {
    const radius = r * scale / 2;
    ctx.save();
    ctx.fillStyle = "#3399e8";

    ctx.beginPath();
    ctx.moveTo(originX - r * scale, originY);
    ctx.lineTo(originX, originY - radius);
    ctx.lineTo(originX, originY);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.arc(originX, originY, radius, Math.PI / 2, Math.PI, false);
    ctx.lineTo(originX, originY);
    ctx.closePath();
    ctx.fill();

    ctx.beginPath();
    ctx.rect(originX, originY, radius, r * scale);
    ctx.fill();
    ctx.restore();
}

function drawAxes(originX, originY, scale, r) {
    ctx.save();
    ctx.strokeStyle = "#111111";
    ctx.fillStyle = "#111111";
    ctx.lineWidth = 1;
    ctx.font = "15px Arial";

    ctx.beginPath();
    ctx.moveTo(20, originY);
    ctx.lineTo(canvas.width - 20, originY);
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(canvas.width - 20, originY);
    ctx.lineTo(canvas.width - 29, originY - 4);
    ctx.moveTo(canvas.width - 20, originY);
    ctx.lineTo(canvas.width - 29, originY + 4);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(originX, canvas.height - 20);
    ctx.lineTo(originX, 20);
    ctx.stroke();
    ctx.beginPath();
    ctx.moveTo(originX, 20);
    ctx.lineTo(originX - 4, 29);
    ctx.moveTo(originX, 20);
    ctx.lineTo(originX + 4, 29);
    ctx.stroke();
    ctx.fillText("x", canvas.width - 13, originY + 5);
    ctx.fillText("y", originX + 8, 18);

    const xTicks = [
        [-r, "-R", -10],
        [-r / 2, "-R/2", -18],
        [r / 2, "R/2", -12],
        [r, "R", -4]
    ];
    xTicks.forEach(function(tick) {
        const x = originX + tick[0] * scale;
        drawTickX(x, originY);
        ctx.fillText(tick[1], x + tick[2], originY - 10);
    });

    const yTicks = [
        [r, "R"],
        [r / 2, "R/2"],
        [-r / 2, "-R/2"],
        [-r, "-R"]
    ];
    yTicks.forEach(function(tick) {
        const y = originY - tick[0] * scale;
        drawTickY(originX, y);
        ctx.fillText(tick[1], originX + 10, y + 5);
    });
    ctx.restore();
}

function drawTickX(x, y) {
    ctx.beginPath();
    ctx.moveTo(x, y - 4);
    ctx.lineTo(x, y + 4);
    ctx.stroke();
}

function drawTickY(x, y) {
    ctx.beginPath();
    ctx.moveTo(x - 4, y);
    ctx.lineTo(x + 4, y);
    ctx.stroke();
}

function drawSavedPoints(originX, originY, scale) {
    ctx.save();
    results.forEach(function(result) {
        const px = originX + result.x * scale;
        const py = originY - result.y * scale;
        if (px < 0 || px > canvas.width || py < 0 || py > canvas.height) return;

        ctx.beginPath();
        ctx.arc(px, py, 5, 0, 2 * Math.PI);
        ctx.fillStyle = result.hit ? "#198754" : "#c62828";
        ctx.fill();
        ctx.strokeStyle = "#111111";
        ctx.lineWidth = 1;
        ctx.stroke();
    });
    ctx.restore();
}

renderResults();
drawCanvas();
sendRequest({ action: "history" })
    .then(function(response) {
        applyServerResponse(response.payload);
        if (response.payload.error) showError(response.payload.error);
    })
    .catch(function(error) {
        showError("Сервер пока недоступен: " + error.message);
    });
