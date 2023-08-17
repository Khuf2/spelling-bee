import { useState, useEffect } from 'react';
/* We should check the model passed from the frontend to see what page this directs to.
* This will only be one page for the moment but there's definitely potential for more.
*/
function SpellingBeePage() {
    // State hook variables
    const [letters, setLetters] = useState([])
    const [coreLetter, setCoreLetter] = useState("")
    const [matrix, setMatrix] = useState([])
    const [prefixes, setPrefixes] = useState({})
    const [summary, setSummary] = useState({})
    const [foundWords, setFoundWords] = useState([])
    const [points, setPoints] = useState(0)
    const [date, setDate] = useState("")

    useEffect(() => {
        getInitialModel();
    }, []);

    function getInitialModel() {
        fetch('/model')
            .then(response => response.json())
            .then(data => {
              console.log("Fetched something.");
              console.log(data);
              console.log(data["matrix"])
              setLetters(data["letters"]);
              setCoreLetter(data["coreLetter"]);
              setMatrix(data["matrix"]);
              setPrefixes(data["prefixes"]);
              setSummary(data["summary"]);
              setFoundWords(data["foundWords"]);
              setPoints(data["points"]);
              setDate(data["date"]);
              console.log(matrix)
            })
    }

    function buildMatrixTable() {
        console.log(matrix)
        var rows = matrix.map(function (item, i) {
            var entry = item.map(function (element, j) {
                return (
                    <td key={j}> {element} </td>
                );
            });
            return (
                <tr key={i}> {entry} </tr>
            );
        });
        return (
            <div>
                <table className="table-hover table-striped table-bordered">
                    <tbody>
                        {rows}
                    </tbody>
                </table>
            </div>
        );
    }

    return (
        <div className="outer container-fluid" style={{border: "2px solid black"}}>
            <div className="row">
                <button onClick={getInitialModel}>TestInitModel</button>
            </div>
            <div className="row">
                <div className="col-md-6 inner">
                    <div className="col-md-12 inner">
                        Summary
                    </div>
                    <div className="col-md-12 inner">
                        Input
                    </div>
                </div>
                <div className="col-md-6 inner">
                    {buildMatrixTable}
                </div>
            </div>
            <div className="row">
                <div className="col-md-6 inner">
                    {letters}
                </div>
                <div className="col-md-6 inner">
                    Prefixes
                </div>
            </div>
            <div className="row">
                <div className="col-md-6 inner">
                    {points}
                </div>
                <div className="col-md-6 inner">
                    Found Words
                </div>
            </div>
        </div>
    );
}

export default SpellingBeePage;