// grabbing IDs from index.html
const recordButton = document.getElementById('recordButton');
const statusText = document.getElementById('status');
const transcriptDiv = document.getElementById('transcript');

let mediaRecorder; // the browser's MediaRecorder instance, created once recording starts
let audioChunks = []; // stores chunks of audio data as they're captured, later combined into one file
let isRecording = false; // tracks whether we're currently recording, so the button knows whether to start or stop

// listening for a click on the record button
recordButton.addEventListener('click', async () => {
    if (!isRecording) { // if not recording start
        await startRecording();
    } else { // if recording stop
        stopRecording();
    }
});

// for mic access and capturing audio
async function startRecording() {
    // Ask the browser for microphone access
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
	// Wrap the mic stream in a MediaRecorder so we can start/stop recording and capture the audio data
	mediaRecorder = new MediaRecorder(stream);
    audioChunks = []; // reset array for new recording

    // store every new chunk of audio data
    mediaRecorder.ondataavailable = (event) => {
        audioChunks.push(event.data); // pushing raw audio data
    };

	// start recording
    mediaRecorder.start();
    isRecording = true; 
	
	// showing that the recording is happening to user
    recordButton.textContent = 'Stop Recording';
    statusText.textContent = 'Recording...';
}

// stop recording and reset everything
function stopRecording() {
    mediaRecorder.stop();
    isRecording = false;

    recordButton.textContent = 'Start Recording';
	statusText.textContent = 'Processing...'; // letting user know audio is being processed

    // mediaRecorder.onstop starts once the recorder has fully finished and all data is available
    mediaRecorder.onstop = async () => {
        // Combine all the captured audio chunks into a audio file
        const audioBlob = new Blob(audioChunks, { type: 'audio/webm' });

        // FormData packages the audio in the multipart format the backend expects
        const formData = new FormData();
        formData.append('audio', audioBlob); // "audio" key must match @RequestParam("audio") in the Java controller

        // Send the audio to backend endpoint and wait for the response
        const response = await fetch('/api/transcribe', {
            method: 'POST',
            body: formData
        });

        // Parse the JSON response body
        const data = await response.json();

        // Display the returned text on the page
        document.getElementById('transcript').textContent = data.text;

        // Recording is complete now reset status so the user can record again
        statusText.textContent = 'Idle';
	};
}