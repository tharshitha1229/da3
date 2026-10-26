const AIRTRACK_API =
    "https://airline-backend-1lhg.onrender.com/api";

async function fetchAirportWithRetry(url, options = {}, retries = 12, delay = 5000) {
    const liveStatus = document.querySelector(".live-status");
    for (let attempt = 0; attempt <= retries; attempt++) {
        try {
            const controller = new AbortController();
            const timeoutId = setTimeout(() => controller.abort(), 10000);
            const response = await fetch(url, {
                ...options,
                signal: options.signal || controller.signal
            });
            clearTimeout(timeoutId);
            if (!response.ok) {
                if (attempt < retries && (response.status === 404 || response.status >= 500)) {
                    if (liveStatus) {
                        const remaining = Math.max(0, (retries - attempt) * Math.round(delay / 1000));
                        liveStatus.innerHTML = `<span class="live-dot" style="background:#eab308;box-shadow:0 0 10px #eab308;"></span> WAKING UP SERVER (${remaining}s)...`;
                        liveStatus.style.color = "#eab308";
                    }
                    await new Promise(r => setTimeout(r, delay));
                    continue;
                }
                throw new Error(response.status);
            }
            if (liveStatus && liveStatus.textContent.includes("WAKING")) {
                liveStatus.innerHTML = `<span class="live-dot"></span> SYSTEM ONLINE`;
                liveStatus.style.color = "";
            }
            return await response.json();
        } catch (err) {
            if (attempt < retries) {
                if (liveStatus) {
                    const remaining = Math.max(0, (retries - attempt) * Math.round(delay / 1000));
                    liveStatus.innerHTML = `<span class="live-dot" style="background:#eab308;box-shadow:0 0 10px #eab308;"></span> WAKING UP SERVER (${remaining}s)...`;
                    liveStatus.style.color = "#eab308";
                }
                await new Promise(r => setTimeout(r, delay));
                continue;
            }
            if (liveStatus) {
                liveStatus.innerHTML = `<span class="live-dot" style="background:#dc2626;box-shadow:none;"></span> SYSTEM OFFLINE`;
                liveStatus.style.color = "#dc2626";
            }
            throw err;
        }
    }
}





function enterDashboard() {

    window.location.href =
        "dashboard.html";

}




function openModule(section) {

    window.location.href =
        `dashboard.html#${section}`;

}




async function loadCounts() {

    const endpoints = {

        passengers:
            "/passengers",

        reservations:
            "/reservations",

        flights:
            "/flights",

        tickets:
            "/tickets",

        baggage:
            "/baggage",

        airports:
            "/airports"

    };


    const elementMap = {

        passengers:
            "count-passengers",

        reservations:
            "count-reservations",

        flights:
            "count-flights",

        tickets:
            "count-tickets",

        baggage:
            "count-baggage",

        airports:
            "count-airports"

    };


    for (
        const [key, endpoint]
        of Object.entries(endpoints)
    ) {

        try {

            const data =
                await fetchAirportWithRetry(
                    AIRTRACK_API + endpoint
                );


            const element =
                document.getElementById(
                    elementMap[key]
                );


            if (element) {

                element.textContent =
                    Array.isArray(data)
                        ? data.length
                        : "—";

            }

        } catch (error) {

            console.error(
                `Failed to load ${key}`,
                error
            );

        }

    }

}




async function loadFlights() {

    const board =
        document.getElementById(
            "flight-list"
        );


    if (!board) {

        return;

    }


    try {

        const flights =
            await fetchAirportWithRetry(
                `${AIRTRACK_API}/flights`
            );


        if (
            !Array.isArray(flights) ||
            flights.length === 0
        ) {

            board.innerHTML = `
                <div class="flight-loading">
                    No flights available.
                </div>
            `;

            return;

        }


        board.innerHTML =
            flights
                .slice(0, 5)
                .map(flight => {

                    return `

                        <div class="flight-row">

                            <strong>
                                ${safe(
                                    flight.flightID
                                )}
                            </strong>

                            <span>
                                ${formatTime(
                                    flight.departureTime
                                )}
                            </span>

                            <span>
                                ${safe(
                                    flight.airlineID
                                )}
                            </span>

                        </div>

                    `;

                })
                .join("");


    } catch (error) {

        console.error(
            "Flight loading failed",
            error
        );


        board.innerHTML = `

            <div class="flight-loading">

                Unable to load flight data.

            </div>

        `;

    }

}




const searchSources = [

    {
        endpoint: "/passengers",
        type: "Passenger",
        icon: "👥",
        section: "passengers"
    },

    {
        endpoint: "/reservations",
        type: "Reservation",
        icon: "▣",
        section: "reservations"
    },

    {
        endpoint: "/flights",
        type: "Flight",
        icon: "✈",
        section: "flights"
    },

    {
        endpoint: "/tickets",
        type: "Ticket",
        icon: "🎫",
        section: "tickets"
    },

    {
        endpoint: "/baggage",
        type: "Baggage",
        icon: "🧳",
        section: "baggage"
    },

    {
        endpoint: "/airports",
        type: "Airport",
        icon: "⌖",
        section: "airports"
    },

    {
        endpoint: "/airlines",
        type: "Airline",
        icon: "A",
        section: "airlines"
    },

    {
        endpoint: "/employees",
        type: "Employee",
        icon: "◉",
        section: "employees"
    }

];


let searchData = [];




async function loadSearchData() {

    const results = [];


    for (
        const source
        of searchSources
    ) {

        try {

            const data =
                await fetchAirportWithRetry(
                    AIRTRACK_API +
                    source.endpoint
                );


            if (
                !Array.isArray(data)
            ) {

                continue;

            }


            data.forEach(record => {

                results.push({

                    type:
                        source.type,

                    icon:
                        source.icon,

                    section:
                        source.section,

                    record

                });

            });


        } catch (error) {

            console.error(
                source.type,
                error
            );

        }

    }


    searchData =
        results;

}




function setupSearch() {

    const input =
        document.getElementById(
            "airport-search"
        );


    const results =
        document.getElementById(
            "search-results"
        );


    if (!input || !results) {

        return;

    }


    input.addEventListener(
        "input",
        () => {

            const query =
                input.value
                    .trim()
                    .toLowerCase();


            if (!query) {

                results.classList.remove(
                    "show"
                );

                results.innerHTML = "";

                return;

            }


            const matches =
                searchData
                    .filter(item => {

                        return Object.values(
                            item.record
                        )
                        .join(" ")
                        .toLowerCase()
                        .includes(query);

                    })
                    .slice(0, 10);


            renderSearchResults(
                matches
            );

        }
    );


    input.addEventListener(
        "keydown",
        event => {

            if (
                event.key === "Escape"
            ) {

                results.classList.remove(
                    "show"
                );

                input.value = "";

            }

        }
    );

}




function renderSearchResults(
    matches
) {

    const results =
        document.getElementById(
            "search-results"
        );


    if (!results) {

        return;

    }


    if (!matches.length) {

        results.innerHTML = `

            <div class="search-result">

                <div class="search-result-icon">
                    ?
                </div>

                <div class="search-result-text">

                    <strong>
                        No records found
                    </strong>

                    <small>
                        Try an ID, name, flight,
                        baggage or reservation.
                    </small>

                </div>

            </div>

        `;

        results.classList.add("show");

        return;

    }


    results.innerHTML =
        matches
            .map((item, index) => {

                const primary =
                    getPrimaryValue(
                        item.record
                    );


                const secondary =
                    getSecondaryValue(
                        item.record
                    );


                return `

                    <button
                        class="search-result"
                        onclick="selectSearchResult(${index})">

                        <div class="search-result-icon">
                            ${item.icon}
                        </div>

                        <div class="search-result-text">

                            <strong>
                                ${safe(primary)}
                            </strong>

                            <small>
                                ${safe(secondary)}
                            </small>

                        </div>

                        <span class="search-result-type">
                            ${item.type}
                        </span>

                    </button>

                `;

            })
            .join("");


    window.currentSearchMatches =
        matches;


    results.classList.add(
        "show"
    );

}




function selectSearchResult(
    index
) {

    const matches =
        window.currentSearchMatches ||
        [];


    const selected =
        matches[index];


    if (!selected) {

        return;

    }


    window.location.href =
        `dashboard.html#${selected.section}`;

}




function getPrimaryValue(
    record
) {

    const priority = [

        "passengerID",
        "reservationID",
        "flightID",
        "ticketNo",
        "baggageID",
        "airportID",
        "airlineID",
        "employeeID"

    ];


    for (
        const key
        of priority
    ) {

        if (
            record[key] !== undefined &&
            record[key] !== null
        ) {

            return record[key];

        }

    }


    return Object.values(record)[0] ||
        "Record";

}




function getSecondaryValue(
    record
) {

    const priority = [

        "name",
        "airportName",
        "airlineName",
        "city",
        "email",
        "airlineID",
        "passengerID",
        "reservationID"

    ];


    for (
        const key
        of priority
    ) {

        if (
            record[key] !== undefined &&
            record[key] !== null
        ) {

            return record[key];

        }

    }


    return "Database record";

}




function formatTime(value) {

    if (!value) {

        return "--:--";

    }


    return String(value)
        .substring(0, 5);

}




function safe(value) {

    if (
        value === null ||
        value === undefined
    ) {

        return "";

    }


    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");

}




function showTeam() {

    document
        .getElementById("team-popup")
        ?.classList
        .add("show");

}


function closeTeam() {

    document
        .getElementById("team-popup")
        ?.classList
        .remove("show");

}




document.addEventListener(
    "keydown",
    event => {

        if (
            (event.ctrlKey ||
             event.metaKey) &&
            event.key.toLowerCase() === "k"
        ) {

            event.preventDefault();


            document
                .getElementById(
                    "airport-search"
                )
                ?.focus();

        }

    }
);




async function waitForAirportBackend(maxWaitMs = 60000, intervalMs = 5000) {
    const liveStatus = document.querySelector(".live-status");
    const startTime = Date.now();
    let countdownInterval = null;

    try {
        while (Date.now() - startTime < maxWaitMs) {
            const timePassed = Date.now() - startTime;
            const remainingSec = Math.max(0, Math.ceil((maxWaitMs - timePassed) / 1000));

            try {
                const controller = new AbortController();
                const timeoutId = setTimeout(() => controller.abort(), 4500);
                const res = await fetch(`${AIRTRACK_API}/airlines`, {
                    method: "GET",
                    signal: controller.signal,
                    headers: { "Content-Type": "application/json" }
                });
                clearTimeout(timeoutId);
                if (res.ok) {
                    if (countdownInterval) clearInterval(countdownInterval);
                    if (liveStatus) {
                        liveStatus.innerHTML = `<span class="live-dot"></span> SYSTEM ONLINE`;
                        liveStatus.style.color = "";
                    }
                    return true;
                }
            } catch (e) {
            }

            if (!countdownInterval) {
                if (liveStatus) {
                    liveStatus.innerHTML = `<span class="live-dot" style="background:#eab308;box-shadow:0 0 10px #eab308;"></span> WAKING UP SERVER (${remainingSec}s)...`;
                    liveStatus.style.color = "#eab308";
                }
                countdownInterval = setInterval(() => {
                    const secLeft = Math.max(0, Math.ceil((maxWaitMs - (Date.now() - startTime)) / 1000));
                    if (secLeft > 0 && liveStatus) {
                        liveStatus.innerHTML = `<span class="live-dot" style="background:#eab308;box-shadow:0 0 10px #eab308;"></span> WAKING UP SERVER (${secLeft}s)...`;
                    }
                }, 1000);
            }

            const sleepDuration = Math.min(intervalMs, Math.max(200, maxWaitMs - (Date.now() - startTime)));
            if (Date.now() - startTime + sleepDuration < maxWaitMs) {
                await new Promise(resolve => setTimeout(resolve, sleepDuration));
            } else {
                break;
            }
        }
    } finally {
        if (countdownInterval) {
            clearInterval(countdownInterval);
        }
    }

    if (liveStatus) {
        liveStatus.innerHTML = `<span class="live-dot" style="background:#dc2626;box-shadow:none;"></span> SYSTEM OFFLINE`;
        liveStatus.style.color = "#dc2626";
    }
    return false;
}

async function initializeAirportHome() {
    setupSearch();
    await waitForAirportBackend(60000, 5000);
    await Promise.all([
        loadCounts(),
        loadFlights(),
        loadSearchData()
    ]);
}

document.addEventListener(
    "DOMContentLoaded",
    initializeAirportHome
);