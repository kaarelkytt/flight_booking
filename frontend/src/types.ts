export type Flight = {
    id: number;
    flightNumber: string;
    departureIATA: string;
    destinationIATA: string;
    departureCity: string;
    destinationCity: string;
    departureTime: string;
    arrivalTime: string;
    durationMinutes: number;
    aircraftType: string;
    initialPrice: number;
    extraLegroomMultiplier: number;
    nearExitMultiplier: number;
};

export type FlightPage = {
    content: Flight[];
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
};

export type Seat = {
    id: number;
    seatNumber: string | null;
    occupied: boolean;
    aisleSeat: boolean;
    windowSeat: boolean;
    extraLegroom: boolean;
    nearExit: boolean;
};

export type SeatRow = {
    seats: Seat[];
};

export type SeatPlan = {
    seatRows: SeatRow[];
};