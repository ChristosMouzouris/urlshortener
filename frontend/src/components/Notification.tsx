import React from 'react';
import { NotificationEnum } from '../types/notificationEnum';

interface Props {
  id: string;
  notificationText: string;
  notificationType: NotificationEnum;
  onClose: (id: string) => void;
}

const Notification: React.FC<Props> = ({
  id,
  notificationText,
  notificationType,
  onClose,
}) => {
  return (
    <div className="relative w-[400px] rounded-2xl bg-white/[0.025] backdrop-blur-md border border-white/10 shadow-lg shadow-black/50 text-center p-6 transition-all duration-300">
      <button
        onClick={() => onClose(id)}
        className="absolute top-2 right-2 text-white hover:text-orange-500 cursor-pointer"
      >
        ✖
      </button>

      <div className="flex items-center justify-center gap-2">
        {notificationType === NotificationEnum.success ? '✅' : '❌'}
        <h2 className="text-white">{notificationText}</h2>
      </div>
    </div>
  );
};

export default Notification;
