-record(unisup_message, {sender,
               receiver,
               text,
               timestamp    % An incremented value or a real date
              }).

-record(unisup_user, {username,
                  password,
                  nodeName,
                  pid
                }).