
/* We should check the model passed from the frontend to see what page this directs to.
* This will only be one page for the moment but there's definitely potential for more.
*/
function SpellingBeePage() {
    function clickButton() {
      fetch('/guest')
            .then(response => response.text())
            .then(data => {
              console.log("Fetched something.")
              console.log(data)
            })
    }

    function clickButton() {
      fetch('/guest')
            .then(response => response.text())
            .then(data => {
              console.log("Fetched something.")
              console.log(data)
            })
    }

    return (
        <div class="outer container-fluid" style={{border: "2px solid black"}}>
            <div class="row">
                <button onClick={clickButton}>Fetch</button>
            </div>
            <div class="row">
                <div class="col-md-6 inner">
                    <div class="col-md-12 inner">
                        WORDS: 41, POINTS: 173, PANGRAMS: 1, BINGO
                    </div>
                    <div class="col-md-12 inner">
                        Input
                    </div>
                </div>
                <div class="col-md-6 inner">
                    Matrix
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 inner">
                    Grid
                </div>
                <div class="col-md-6 inner">
                    Prefixes
                </div>
            </div>
            <div class="row">
                <div class="col-md-6 inner">
                    Points
                </div>
                <div class="col-md-6 inner">
                    Found words
                </div>
            </div>
        </div>
    );
}

export default SpellingBeePage;