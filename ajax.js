<script>
        function verifyPaiement(id_devis) {
            const montant = document.getElementById('montant' + id_devis).value;
            const montant_ajax_error = document.getElementById("montant_ajax_error" + id_devis);

            if ((montant.trim() == '') === true) {
                montant_ajax_error.style.display = 'block';
                montant_ajax_error.innerHTML = `<p>Veuillez fournir un montant</p>`;
            } else {
                var xhr = newXhr();
                xhr.addEventListener("load", function() {

                    var resultat = JSON.parse(xhr.responseText);
                    if (resultat.ok == 0) {
                        montant_ajax_error.style.display = 'block';

                        const formatter = new Intl.NumberFormat('mg-MG', {
                            style: 'currency',
                            currency: 'MGA',
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                        });

                        montant_ajax_error.innerHTML = `<p>Avec ce montant ${formatter.format(resultat.montant)}</p><p> votre paiement total ${formatter.format(resultat.nouveau_montant_total)}</p><p>a dépassé le montant total du devis ${formatter.format(resultat.prix_total_devis)}</p>`;
                    } else {
                        montant_ajax_error.innerHTML = '';
                        montant_ajax_error.style.display = 'none';
                        document.getElementById('paiement_form' + id_devis).submit();
                    }

                    // return chargeProduit;
                });

                xhr.open("POST", 'client/devis/verify_paiement');
                //envoie du formulaire fictif
                const formHTML = document.createElement("form");
                //numero
                var input = document.createElement("input");
                input.setAttribute('type', 'hidden');
                input.setAttribute('name', 'id_devis');
                input.value = id_devis;
                formHTML.appendChild(input);
                //idproduit
                input = document.createElement("input");
                input.setAttribute('type', 'hidden');
                input.setAttribute('name', 'montant');
                input.value = montant;
                formHTML.appendChild(input);
                const formJSON = new FormData(formHTML);
                xhr.send(formJSON);
            }
        }

        function newXhr() {
            var xhr;
            try {
                xhr = new ActiveXObject('Msxml2.XMLHTTP');
            } catch (e) {
                try {
                    xhr = new ActiveXObject('Microsoft.XMLHTTP');
                } catch (e2) {
                    try {
                        xhr = new XMLHttpRequest();
                    } catch (e3) {
                        xhr = false;
                    }
                }
            }

            return xhr;
        }
    </script>