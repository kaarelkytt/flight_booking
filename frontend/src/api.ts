export const fetchFlights = async (query: string) => {
    const response = await fetch(`http://localhost:8080/flights/search${query}`);
    if (!response.ok) {
        throw new Error("Error fetching flights");
    }
    return response.json();
};

export const fetchSeatMap = async (flightId: number) => {
    const response = await fetch(`http://localhost:8080/flights/${flightId}/seats`);
    if (!response.ok) {
        throw new Error("Error fetching seat map");
    }
    return response.json();
};