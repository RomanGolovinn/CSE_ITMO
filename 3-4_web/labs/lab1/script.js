const STORAGE_KEY = "web-lab1-results";

const canvas =
    document.getElementById("coordinateCanvas");

const ctx =
    canvas.getContext("2d");

const form =
    document.getElementById("pointForm");

const xInput =
    document.getElementById("x");

const yInput =
    document.getElementById("y");

const rInput =
    document.getElementById("r");

const xError =
    document.getElementById("xError");

const yError =
    document.getElementById("yError");

const rError =
    document.getElementById("rError");

const formMessage =
    document.getElementById("formMessage");

const resultsBody =
    document.getElementById("resultsBody");

const clearResultsButton =
    document.getElementById("clearResults");

let results = loadResults();

function loadResults() {
    try {
        const saved =
            localStorage.getItem(STORAGE_KEY);

        if (saved === null) {
            return [];
        }

        const parsed =
            JSON.parse(saved);

        if (Array.isArray(parsed)) {
            return parsed;
        }

        return [];

    } catch (error) {
        console.error(
            "Ошибка чтения LocalStorage:",
            error
        );
        return [];
    }
}

function saveResults() {
    localStorage.setItem(
        STORAGE_KEY,
        JSON.stringify(results)
    );
}

function parseNumber(value) {
    const normalized =
        value
            .trim()
            .replace(",", ".");

    if (normalized === "") {
        return null;
    }

    const number =
        Number(normalized);

    if (!Number.isFinite(number)) {
        return null;
    }

    return number;
}

function clearErrors() {
    xError.textContent = "";
    yError.textContent = "";
    rError.textContent = "";
    formMessage.textContent = "";
    formMessage.className =
        "form-message";
}

function validateForm() {
    clearErrors();

    const x =
        parseNumber(xInput.value);

    const y =
        parseNumber(yInput.value);

    const r =
        Number(rInput.value);

    let valid = true;

    if (x === null) {
        xError.textContent =
            "Введите число.";
        valid = false;
    } else if (x < -5 || x > 5) {
        xError.textContent =
            "X должен быть от -5 до 5.";
        valid = false;
    }

    if (y === null) {
        yError.textContent =
            "Введите число.";
        valid = false;
    } else if (y < -3 || y > 3) {
        yError.textContent =
            "Y должен быть от -3 до 3.";
        valid = false;
    }

    if (![1, 2, 3, 4, 5].includes(r)) {
        rError.textContent =
            "R должен быть 1, 2, 3, 4 или 5.";
        valid = false;
    }

    return {
        valid: valid,
        x: x,
        y: y,
        r: r
    };
}

function isInside(x, y, r) {
    const inQuarterCircle =
        x >= -r / 2 &&
        x <= 0 &&
        y >= 0 &&
        y <= r / 2 &&
        x * x + y * y <=
            (r * r) / 4;

    const inTriangle =
        x >= 0 &&
        x <= r &&
        y >= 0 &&
        y <= r / 2 - x / 2;

    const inRectangle =
        x >= 0 &&
        x <= r &&
        y >= -r / 2 &&
        y <= 0;

    return (
        inQuarterCircle ||
        inTriangle ||
        inRectangle
    );
}

function formatDate(timestamp) {
    return new Intl.DateTimeFormat(
        "ru-RU",
        {
            dateStyle: "short",
            timeStyle: "medium"
        }
    ).format(
        new Date(timestamp)
    );
}

function renderResults() {
    resultsBody.innerHTML = "";

    if (results.length === 0) {
        const row =
            document.createElement("tr");

        row.className =
            "empty-row";

        row.innerHTML = `
            <td colspan="5">
                История проверок пуста.
            </td>
        `;

        resultsBody.appendChild(row);

        return;
    }

    [...results]
        .reverse()
        .forEach(function(result) {

            const row =
                document.createElement("tr");

            let resultHTML;

            if (result.hit) {
                resultHTML = `
                    <span class="result-hit">
                        Попадание
                    </span>
                `;
            } else {
                resultHTML = `
                    <span class="result-miss">
                        Промах
                    </span>
                `;
            }

            row.innerHTML = `

                <td>
                    ${escapeHtml(String(result.x))}
                </td>

                <td>
                    ${escapeHtml(String(result.y))}
                </td>

                <td>
                    ${escapeHtml(String(result.r))}
                </td>

                <td>
                    ${resultHTML}
                </td>

                <td>
                    ${formatDate(result.timestamp)}
                </td>

            `;

            resultsBody.appendChild(row);

        });
}

function escapeHtml(value) {
    return value
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function drawCanvas() {
    const originX = 340;

    const originY = 250;

    const scale = 50;

    const r =
        Number(rInput.value);

    ctx.clearRect(
        0,
        0,
        canvas.width,
        canvas.height
    );

    drawRegion(
        originX,
        originY,
        scale,
        r
    );

    drawAxes(
        originX,
        originY,
        scale,
        r
    );

    drawSavedPoints(
        originX,
        originY,
        scale
    );
}

function drawRegion(
    originX,
    originY,
    scale,
    r
) {
    ctx.save();

    ctx.fillStyle =
        "#3399e8";

    const radius =
        r * scale / 2;

    const width =
        r * scale;

    const halfHeight =
        r * scale / 2;

    ctx.beginPath();

    ctx.moveTo(
        originX,
        originY
    );

    ctx.lineTo(
        originX - radius,
        originY
    );

    ctx.arc(
        originX,
        originY,
        radius,
        Math.PI,
        Math.PI * 1.5,
        false
    );

    ctx.closePath();

    ctx.fill();

    ctx.beginPath();

    ctx.moveTo(
        originX,
        originY
    );

    ctx.lineTo(
        originX,
        originY - halfHeight
    );

    ctx.lineTo(
        originX + width,
        originY
    );

    ctx.closePath();

    ctx.fill();

    ctx.beginPath();

    ctx.rect(
        originX,
        originY,
        width,
        halfHeight
    );

    ctx.fill();

    ctx.restore();
}

function drawAxes(
    originX,
    originY,
    scale,
    r
) {
    ctx.save();

    ctx.strokeStyle =
        "#111111";

    ctx.fillStyle =
        "#111111";

    ctx.lineWidth = 1;

    ctx.font =
        "15px Arial";

    ctx.beginPath();

    ctx.moveTo(
        20,
        originY
    );

    ctx.lineTo(
        canvas.width - 20,
        originY
    );

    ctx.stroke();

    ctx.beginPath();

    ctx.moveTo(
        canvas.width - 20,
        originY
    );

    ctx.lineTo(
        canvas.width - 29,
        originY - 4
    );

    ctx.moveTo(
        canvas.width - 20,
        originY
    );

    ctx.lineTo(
        canvas.width - 29,
        originY + 4
    );

    ctx.stroke();

    ctx.beginPath();

    ctx.moveTo(
        originX,
        canvas.height - 20
    );

    ctx.lineTo(
        originX,
        20
    );

    ctx.stroke();

    ctx.beginPath();

    ctx.moveTo(
        originX,
        20
    );

    ctx.lineTo(
        originX - 4,
        29
    );

    ctx.moveTo(
        originX,
        20
    );

    ctx.lineTo(
        originX + 4,
        29
    );

    ctx.stroke();

    ctx.fillText(
        "x",
        canvas.width - 13,
        originY + 5
    );

    ctx.fillText(
        "y",
        originX + 8,
        18
    );

    drawTickX(
        originX - r * scale,
        originY
    );

    ctx.fillText(
        "-R",
        originX - r * scale - 10,
        originY - 10
    );

    drawTickX(
        originX - r * scale / 2,
        originY
    );

    ctx.fillText(
        "-R/2",
        originX - r * scale / 2 - 18,
        originY - 10
    );

    drawTickX(
        originX + r * scale / 2,
        originY
    );

    ctx.fillText(
        "R/2",
        originX + r * scale / 2 - 12,
        originY - 10
    );

    drawTickX(
        originX + r * scale,
        originY
    );

    ctx.fillText(
        "R",
        originX + r * scale - 4,
        originY - 10
    );

    drawTickY(
        originX,
        originY - r * scale
    );

    ctx.fillText(
        "R",
        originX + 10,
        originY - r * scale + 5
    );

    drawTickY(
        originX,
        originY - r * scale / 2
    );

    ctx.fillText(
        "R/2",
        originX + 10,
        originY - r * scale / 2 + 5
    );

    drawTickY(
        originX,
        originY + r * scale / 2
    );

    ctx.fillText(
        "-R/2",
        originX + 10,
        originY + r * scale / 2 + 5
    );

    drawTickY(
        originX,
        originY + r * scale
    );

    ctx.fillText(
        "-R",
        originX + 10,
        originY + r * scale + 5
    );

    ctx.restore();
}

function drawTickX(x, y) {
    ctx.beginPath();

    ctx.moveTo(
        x,
        y - 4
    );

    ctx.lineTo(
        x,
        y + 4
    );

    ctx.stroke();
}

function drawTickY(x, y) {
    ctx.beginPath();

    ctx.moveTo(
        x - 4,
        y
    );

    ctx.lineTo(
        x + 4,
        y
    );

    ctx.stroke();
}

function drawSavedPoints(
    originX,
    originY,
    scale
) {
    ctx.save();

    results.forEach(
        function(result) {

            const px =
                originX +
                result.x * scale;

            const py =
                originY -
                result.y * scale;

            if (
                px < 0 ||
                px > canvas.width ||
                py < 0 ||
                py > canvas.height
            ) {
                return;
            }

            ctx.beginPath();

            ctx.arc(
                px,
                py,
                5,
                0,
                2 * Math.PI
            );

            if (result.hit) {
                ctx.fillStyle =
                    "#198754";
            } else {
                ctx.fillStyle =
                    "#c62828";
            }

            ctx.fill();

            ctx.strokeStyle =
                "#111111";

            ctx.lineWidth = 1;

            ctx.stroke();
        }
    );

    ctx.restore();
}

rInput.addEventListener(
    "change",
    function() {
        drawCanvas();
    }
);

form.addEventListener(
    "submit",
    function(event) {

        event.preventDefault();

        const validation =
            validateForm();

        if (!validation.valid) {

            formMessage.textContent =
                "Исправьте ошибки в форме.";

            formMessage.className =
                "form-message error";

            return;
        }

        const x =
            validation.x;

        const y =
            validation.y;

        const r =
            validation.r;

        const hit =
            isInside(
                x,
                y,
                r
            );

        const result = {

            x: x,

            y: y,

            r: r,

            hit: hit,

            timestamp: Date.now()
        };

        results.push(result);

        saveResults();

        renderResults();

        drawCanvas();

        if (hit) {

            formMessage.textContent =
                "Точка попала в заданную область.";

            formMessage.className =
                "form-message success";

        } else {

            formMessage.textContent =
                "Точка не попала в заданную область.";

            formMessage.className =
                "form-message error";
        }
    }
);

clearResultsButton.addEventListener(
    "click",
    function() {

        results = [];

        saveResults();

        renderResults();

        drawCanvas();

        formMessage.textContent =
            "История проверок очищена.";

        formMessage.className =
            "form-message success";
    }
);

renderResults();

drawCanvas();