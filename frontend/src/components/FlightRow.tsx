import "../styles/FlightRow.css";
import { format } from "date-fns";
import { parseISO } from "date-fns";

function formatDate(dateTime: string) {
    return format(parseISO(dateTime), "(dd.MM.yyyy)");
}

function formatTime(dateTime: string) {
    return format(parseISO(dateTime), "HH:mm");
}

type FlightProps = {
    index: number;
    flightNumber: string;
    departureCity: string;
    destinationCity: string;
    departureTime: string;
    arrivalTime: string;
    durationMinutes: number;
    initialPrice: number;
    onSelect: () => void;
};

export default function FlightRow({
                                      index,
                                      flightNumber,
                                      departureCity,
                                      destinationCity,
                                      departureTime,
                                      arrivalTime,
                                      durationMinutes,
                                      initialPrice,
                                      onSelect
                                  }: FlightProps) {
    return (
        <div className="flight-row" onClick={onSelect}>
            <span className="flight-index">{index}.</span>
            <div className="flight-departure">
                <div className="city">
                    <span>{departureCity}</span>
                </div>
                <div className="dateTime">
                    <span>{formatTime(departureTime)}</span> <span>{formatDate(departureTime)}</span>
                </div>
            </div>
            <div className="flight-info">
                <div  className="flight-duration">
                    <span>{durationMinutes} min</span>
                </div>
                <div className="line"></div>
                <div  className="flight-number">
                    <span>{flightNumber}</span>
                </div>
            </div>
            <div className="flight-destination">
                <div className="city">
                    <span>{destinationCity}</span>
                </div>
                <div className="dateTime">
                    <span>{formatTime(arrivalTime)}</span> <span>{formatDate(departureTime)}</span>
                </div>
            </div>
            <div className="price">
                {initialPrice.toFixed(2)} €
            </div>
        </div>
    );
}