document.addEventListener('DOMContentLoaded', function() {
    // Get DOM elements
    const modal = document.getElementById('termsModal');
    const termsLink = document.getElementById('termsLink');
    const closeBtn = document.querySelector('.close');
    const acceptBtn = document.getElementById('acceptTerms');
    const termsCheckbox = document.getElementById('termsAgreed');
    const medicalRadios = document.getElementsByName('hasMedicalCondition');
    const medicalDetailsGroup = document.getElementById('medicalDetailsGroup');
    const registrationForm = document.getElementById('registrationForm');

    // Terms modal functionality
    termsLink.addEventListener('click', function(e) {
        e.preventDefault(); // Prevent default button behavior
        modal.style.display = 'block';
    });

    closeBtn.addEventListener('click', function() {
        modal.style.display = 'none';
    });

    acceptBtn.addEventListener('click', function() {
        termsCheckbox.checked = true;
        modal.style.display = 'none';
    });

    // Close modal when clicking outside
    window.addEventListener('click', function(event) {
        if (event.target == modal) {
            modal.style.display = 'none';
        }
    });

    // Toggle medical details section
    medicalRadios.forEach(radio => {
        radio.addEventListener('change', function() {
            medicalDetailsGroup.style.display = this.value === 'true' ? 'block' : 'none';
            
            // If showing medical details, make the field required
            const medicalDetails = document.getElementById('medicalDetails');
            if (this.value === 'true') {
                medicalDetails.setAttribute('required', '');
            } else {
                medicalDetails.removeAttribute('required');
            }
        });
    });

    // Form submission
    registrationForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Validate form
        if (validateForm()) {
            // Here you would typically send the data to your server
            alert('Registration submitted successfully! A confirmation email will be sent shortly.');
            
            // For demo purposes - you would replace this with actual form submission
            // this.submit();
        }
    });

    // Form validation function
    function validateForm() {
        let isValid = true;
        
        // Basic validation example - you can expand this
        const email = document.getElementById('email').value;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if (!emailRegex.test(email)) {
            alert('Please enter a valid email address');
            isValid = false;
        }
        
        // Check terms agreement
        if (!termsCheckbox.checked) {
            alert('You must agree to the terms and conditions');
            isValid = false;
        }
        
        return isValid;
    }
});