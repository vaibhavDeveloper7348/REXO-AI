# Jack AI API - FIXED VERSION
# Properly returns URL and query data
# pip install flask flask-cors

from flask import Flask, request, jsonify
from flask_cors import CORS
import json
import os
from difflib import SequenceMatcher

app = Flask(__name__)
CORS(app)

class JackAssistant:
    def __init__(self):
        # FAQ question/answer pairs now live in faqs.json (kept next to this file)
        self.faqs = self._load_faqs()
        
        self.questions = list(self.faqs.keys())
        
        # College FAQ shortcuts
        self.college_faq_answers = {
            "btech programs": "GNDEC offers UG, PG, and PhD programs in engineering, management, and sciences.",
            "btech duration": "The B.Tech program at GNDEC lasts for 4 years.",
            "mtech specializations": "M.Tech specializations include CSE, Mechanical, Electrical, Structural, and more.",
            "departments": "GNDEC has departments in Civil, Mechanical, Electrical, CS, IT, Business, and more.",
            "btech fees": "The first-year B.Tech fee is approximately rupees 96,400.",
            "lateral entry": "Yes, GNDEC allows lateral entry into B.Tech for diploma holders.",
            "hostel facilities": "Yes, GNDEC provides hostel facilities for students."
        }

    def _load_faqs(self):
        """
        Load the FAQ question/answer pairs from faqs.json.
        The JSON file is expected to sit in the same folder as this script.
        """
        faqs_path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "faqs.json")
        with open(faqs_path, "r", encoding="utf-8") as f:
            return json.load(f)

    def get_answer(self, user_question):
        """Find best matching answer"""
        user_question = user_question.lower()
        
        best_match = None
        best_score = 0
        
        for question in self.questions:
            similarity = SequenceMatcher(None, user_question, question).ratio()
            user_words = set(user_question.split())
            question_words = set(question.split())
            word_overlap = len(user_words & question_words) / max(len(user_words), 1)
            score = (similarity * 0.6) + (word_overlap * 0.4)
            
            if score > best_score:
                best_score = score
                best_match = question
        
        if best_score >= 0.3:
            return self.faqs[best_match]
        else:
            return "Sorry, I don't have an answer to that question."

    def parse_command(self, command):
        """
        Parse command and return proper action with data
        Returns: dict with action, message, url, query
        """
        cmd = command.lower().strip()
        
        # Camera commands
        if "camera" in cmd:
            if "open" in cmd or "start" in cmd:
                return {
                    "action": "OPEN_CAMERA",
                    "message": "Opening camera on your mobile"
                }
            elif "close" in cmd or "stop" in cmd:
                return {
                    "action": "CLOSE_CAMERA",
                    "message": "Closing camera"
                }
        
        # YouTube - specific handling
        if "youtube" in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening YouTube",
                    "url": "https://www.youtube.com"
                }
        
        # Facebook
        if "facebook" in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening Facebook",
                    "url": "https://www.facebook.com"
                }
        
        # Instagram
        if "instagram" in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening Instagram",
                    "url": "https://www.instagram.com"
                }
        
        # Twitter/X
        if "twitter" in cmd or " x " in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening Twitter",
                    "url": "https://www.twitter.com"
                }
        
        # LinkedIn
        if "linkedin" in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening LinkedIn",
                    "url": "https://www.linkedin.com"
                }
        
        # GitHub
        if "github" in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening GitHub",
                    "url": "https://www.github.com"
                }
        
        # Browser opening
        if "browser" in cmd or ("open" in cmd and "google" in cmd):
            return {
                "action": "OPEN_BROWSER",
                "message": "Opening browser",
                "url": "https://www.google.com"
            }
        
        if "close" in cmd and "browser" in cmd:
            return {
                "action": "CLOSE_BROWSER",
                "message": "Closing browser"
            }
        
        # Search commands - FIXED
        if "search" in cmd:
            # Extract search query
            query = cmd.replace("search", "").replace("for", "").strip()
            
            # Remove common words
            query = query.replace("on google", "").replace("google", "").strip()
            
            if query:
                return {
                    "action": "SEARCH_GOOGLE",
                    "message": f"Searching for {query}",
                    "query": query
                }
            else:
                return {
                    "action": "FAQ_ANSWER",
                    "answer": "What do you want me to search for?"
                }
        ################################################################################################################################################
        # Timetable commands
        if "timetable" in cmd or "time table" in cmd:

            if "applied" in cmd or "science" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening Applied Science timetable",
                    "url": "https://appsc.gndec.ac.in/time_tables"
                }
        
            elif " it " in f" {cmd} " or "information technology" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening IT timetable",
                    "url": "https://it.gndec.ac.in/?q=node/5"
                }
        
            elif "cse" in cmd or "computer science" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening CSE timetable",
                    "url": "https://cse.gndec.ac.in/?q=node/5"
                }
        
            elif "ece" in cmd or "electronics" in cmd:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening ECE timetable",
                    "url": "https://ece.gndec.ac.in/?q=node/5"
                }
        
            elif "civil" in cmd or " ce " in f" {cmd} ":
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening Civil timetable",
                    "url": "https://ce.gndec.ac.in/?q=node/5"
                }
        
            elif "mechanical" in cmd or " me " in f" {cmd} ":
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening Mechanical timetable",
                    "url": "https://me.gndec.ac.in/?q=node/5"
                }
        
            elif "electrical" in cmd or " ee " in f" {cmd} ":
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening Electrical timetable",
                    "url": "https://ee.gndec.ac.in/?q=node/5"
                }
        
            else:
                return {
                    "action": "OPEN_WEBSITE",
                    "message": "Opening timetable page",
                    "url": "https://appsc.gndec.ac.in/time_tables"
                }
        #########################################################################################################################################
        # Website opening - general pattern
        if "open" in cmd:
            words = cmd.split()
            try:
                open_index = words.index("open")
                if open_index + 1 < len(words):
                    website = words[open_index + 1]
                    
                    # Handle common websites
                    if website in ["google", "youtube", "facebook", "instagram", 
                                   "twitter", "linkedin", "github", "amazon", "netflix"]:
                        url = f"https://www.{website}.com"
                        return {
                            "action": "OPEN_WEBSITE",
                            "message": f"Opening {website}",
                            "url": url
                        }
                    # Handle URLs
                    elif "." in website:
                        url = website if website.startswith("http") else f"https://{website}"
                        return {
                            "action": "OPEN_WEBSITE",
                            "message": f"Opening {website}",
                            "url": url
                        }
            except:
                pass
        
        # Application commands
        if "calculator" in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_APP",
                    "message": "Opening calculator",
                    "app": "calculator"
                }
            elif "close" in cmd:
                return {
                    "action": "CLOSE_APP",
                    "message": "Closing calculator",
                    "app": "calculator"
                }
        
        if "settings" in cmd and "open" in cmd:
            return {
                "action": "OPEN_APP",
                "message": "Opening settings",
                "app": "settings"
            }
        
        if "contacts" in cmd and "open" in cmd:
            return {
                "action": "OPEN_APP",
                "message": "Opening contacts",
                "app": "contacts"
            }
        
        if "gallery" in cmd or "photos" in cmd:
            if "open" in cmd:
                return {
                    "action": "OPEN_APP",
                    "message": "Opening gallery",
                    "app": "gallery"
                }
        
        # Default: treat as FAQ question
        answer = self.get_answer(command)
        return {
            "action": "FAQ_ANSWER",
            "answer": answer
        }

# Initialize Jack Assistant
jack = JackAssistant()

# ============= API ENDPOINTS =============

@app.route('/jack/health', methods=['GET'])
def health_check():
    """Health check endpoint"""
    return jsonify({
        'status': 'online',
        'message': 'Jack AI is running',
        'version': '3.1',
        'mode': 'mobile_first_fixed',
        'features': ['faq', 'camera', 'browser', 'search', 'applications', 'websites']
    })

@app.route('/jack/ask', methods=['POST'])
def ask_question():
    """Ask Jack a question"""
    try:
        data = request.get_json()
        question = data.get('question', '')
        
        if not question:
            return jsonify({'error': 'No question provided'}), 400
        
        answer = jack.get_answer(question)
        
        return jsonify({
            'question': question,
            'answer': answer,
            'action': 'FAQ_ANSWER',
            'status': 'success'
        })
    
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/jack/command', methods=['POST'])
def process_command():
    """
    Process natural language commands
    Returns properly formatted response with action, message, url, query
    """
    try:
        data = request.get_json()
        command = data.get('command', '')
        
        if not command:
            return jsonify({'error': 'No command provided'}), 400
        
        # Parse command and get result
        result = jack.parse_command(command)
        
        # Build response
        response = {
            'command': command,
            'status': 'success'
        }
        
        # Add all fields from parse result
        if 'action' in result:
            response['action'] = result['action']
        
        if 'message' in result:
            response['message'] = result['message']
        
        if 'answer' in result:
            response['answer'] = result['answer']
        
        if 'url' in result:
            response['url'] = result['url']
        
        if 'query' in result:
            response['query'] = result['query']
        
        if 'app' in result:
            response['app'] = result['app']
        
        return jsonify(response)
    
    except Exception as e:
        return jsonify({
            'error': str(e), 
            'status': 'failed'
        }), 500

@app.route('/jack/add_faq', methods=['POST'])
def add_faq():
    """Add new FAQ"""
    try:
        data = request.get_json()
        question = data.get('question', '').lower()
        answer = data.get('answer', '')
        
        if not question or not answer:
            return jsonify({'error': 'Question and answer required'}), 400
        
        jack.faqs[question] = answer
        jack.questions = list(jack.faqs.keys())
        
        return jsonify({
            'message': 'FAQ added successfully',
            'question': question,
            'status': 'success'
        })
    
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/jack/get_all_faqs', methods=['GET'])
def get_all_faqs():
    """Get all FAQs"""
    try:
        return jsonify({
            'faqs': jack.faqs,
            'total_count': len(jack.faqs),
            'status': 'success'
        })
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    import os
    port = int(os.environ.get('PORT', 5001))
    app.run(host='0.0.0.0', port=port, debug=False)
